package com.monolithicbank.customer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monolithicbank.account.entity.Account;
import com.monolithicbank.account.service.AccountService;
import com.monolithicbank.common.exception.BusinessException;
import com.monolithicbank.customer.entity.Customer;
import com.monolithicbank.customer.repository.CustomerRepository;
import com.monolithicbank.transfer.entity.TransferLimit;
import com.monolithicbank.transfer.service.TransferService;

@Service("customerService")
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransferService transferService;

    @Autowired
    AccountService accountService;

    @Transactional(rollbackFor = Exception.class)
    public int createCustomer(Customer customer) throws Exception {
        String cstmId = customer.getCstmId();
        int result = 0;

        if (existsCustomerId(cstmId))
            throw new BusinessException("ID already exists.");
        
        result = customerRepository.insertCustomer(customer);

        try {
            // Set default transfer limits
            TransferLimit transferLimit = TransferLimit.builder()
                .cstmId(customer.getCstmId())
                .oneDyTrnfLmt(500000000L)
                .oneTmTrnfLmt(500000000L)
                .build();
            transferService.createTransferLimit(transferLimit);
        } catch (Exception e) {
            logger.error("Failed to set transfer limits: " + e.getMessage());
            throw new BusinessException("Failed to set transfer limits: " + e.getMessage());
        }

        return result;
    }

    public Customer retrieveCustomer(String cstmId) throws Exception {
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
  
        if (!existsCustomerId(cstmId)) 
            throw new BusinessException("ID does not exist.");

        customer = customerRepository.selectCustomer(customer);

        if (customer == null) 
            throw new BusinessException("Failed to retrieve customer data.");
        
        return customer;
    }

    public Customer retrieveCustomerDetail(String cstmId) throws Exception {
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
        
        if (!existsCustomerId(cstmId)) 
            throw new BusinessException("ID does not exist.");

        customer = customerRepository.selectCustomer(customer);
        if (customer == null) 
            throw new BusinessException("Failed to retrieve customer data.");

        try {
            TransferLimit transferLimit = transferService.retrieveTransferLimit(cstmId);
            customer.setOneDyTrnfLmt(transferLimit.getOneDyTrnfLmt());
            customer.setOneTmTrnfLmt(transferLimit.getOneTmTrnfLmt());
        } catch (Exception e) {
            logger.error("Failed to retrieve transfer limits: " + e.getMessage());
            // Continue execution without throwing an exception
        }

        try {
            List<Account> accountList = accountService.retrieveAccountList(cstmId);
            customer.addAllAccounts(accountList);
        } catch (Exception e) {
            logger.error("Failed to retrieve account list: " + e.getMessage());
            // Continue execution without throwing an exception
        }
        
        return customer;
    }

    public boolean existsCustomerId(String cstmId) throws Exception {
        boolean ret = false;
        Customer customer = new Customer();
        customer.setCstmId(cstmId);
        if (customerRepository.existsCustomer(customer) > 0)
            ret = true;
        return ret;
    }
}