package com.monolithicbank.account.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monolithicbank.account.entity.Account;
import com.monolithicbank.account.entity.TransactionHistory;
import com.monolithicbank.account.entity.TransactionResult;
import com.monolithicbank.account.repository.AccountRepository;
import com.monolithicbank.common.exception.BusinessException;
import com.monolithicbank.customer.entity.Customer;
import com.monolithicbank.customer.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("accountService")
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired private AccountRepository accountRepository;
    @Autowired private CustomerService customerService;

    public Account retrieveAccount(String acntNo) throws Exception {
        Account account = accountRepository.selectAccount(Account.ofAcntNo(acntNo));

        if (account == null)
            throw new BusinessException("Account number does not exist.");

        return account;
    }

    public boolean existsAccountNumber(String acntNo) throws Exception {
        boolean ret = false;
        
        if (accountRepository.selectAccount(Account.ofAcntNo(acntNo)) != null)
            ret = true;
            
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer createAccount(Account account) throws Exception {
        int result = 0;

        // 1) Verify account number duplication
        if(existsAccountNumber(account.getAcntNo()))
            throw new BusinessException("Account number already exists.");

        // 2) Retrieve customer information (to store 'customer name' in the account table)
        Customer customer = customerService.retrieveCustomer(account.getCstmId());
        account.setCstmNm(customer.getCstmNm());
        
        // 3) Create account
        result = accountRepository.insertAccount(account);
        accountRepository.insertTransactionHistoryData(TransactionHistory.builder()
                .acntNo(account.getAcntNo())
                .divCd("D")
                .stsCd("1")
                .trnsBrnch("Yeoksam Main Branch")
                .trnsAmt(account.getAcntBlnc())
                .acntBlnc(account.getAcntBlnc())
                .build());

        return result;
    }

    public List<Account> retrieveAccountList(String cstmId) throws Exception {
        return accountRepository.selectAccountList(Account.ofCstmId(cstmId));
    }

    public Long retrieveAccountBalance(String acntNo) throws Exception {
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .acntNo(acntNo).build();

        Long balance = accountRepository.selectCurrentAccountBalance(transactionHistory);

        if (balance == null)
            return 0L;
        else
            return balance;
    }

    @Transactional(rollbackFor = Exception.class)
    public int createTransactionHistory(TransactionHistory transactionHistory) throws Exception {
        return accountRepository.insertTransactionHistoryData(transactionHistory);
    }

    public List<TransactionHistory> retrieveTransactionHistoryList(String acntNo) throws Exception {
        return accountRepository.selectTransactionHistoryList(TransactionHistory.ofAcntNo(acntNo));
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResult deposit(TransactionHistory transactionHistory) throws Exception {
        String acntNo = transactionHistory.getAcntNo();
        Long trnsAmt = transactionHistory.getTrnsAmt();
        
        logger.info("Deposit request - Account: {}, Amount: {}", acntNo, trnsAmt);
        
        try {
            // 1) Validate input
            if (acntNo == null || acntNo.trim().isEmpty()) {
                throw new BusinessException("Account number is required.");
            }
            
            if (trnsAmt == null || trnsAmt <= 0) {
                throw new BusinessException("Transaction amount must be greater than zero.");
            }
            
            // 입금 금액 제한 (10억원)
            if (trnsAmt > 1_000_000_000L) {
                throw new BusinessException("Transaction amount exceeds the maximum limit (1 billion KRW).");
            }
            
            // 2) Check if account exists
            Account account = retrieveAccount(acntNo);
            if (account == null) {
                throw new BusinessException("Account does not exist: " + acntNo);
            }

            // 3) Retrieve account balance
            Long acntBlnc = retrieveAccountBalance(acntNo);
            logger.info("Current balance for account {}: {}", acntNo, acntBlnc);

            // 4) Create deposit transaction history
            transactionHistory.setAcntBlnc(acntBlnc + trnsAmt);
            transactionHistory.setDivCd("D");
            transactionHistory.setStsCd("1");
            
            logger.info("Creating transaction history: {}", transactionHistory);
            int result = createTransactionHistory(transactionHistory);
            logger.info("Transaction history created with result: {}", result);

            TransactionResult transactionResult = TransactionResult.builder()
                    .acntNo(acntNo)
                    .seq(transactionHistory.getSeq())  // Store seq assigned when creating transaction history
                    .formerBlnc(acntBlnc)
                    .trnsAmt(trnsAmt)
                    .acntBlnc(transactionHistory.getAcntBlnc())
                    .build();
            
            logger.info("Deposit completed successfully: {}", transactionResult);
            return transactionResult;
        } catch (Exception e) {
            logger.error("Error during deposit processing", e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResult withdrawOwnBankOrTransferOtherBank(TransactionHistory transactionHistory) throws Exception {
        String acntNo = transactionHistory.getAcntNo();
        Long trnsAmt = transactionHistory.getTrnsAmt();

        // 1) Retrieve account balance
        Long acntBlnc = retrieveAccountBalance(acntNo);

        // 2) Check if withdrawal is possible
        if (acntBlnc < trnsAmt)
            throw new BusinessException("Insufficient account balance.");
        
        // 3) Create withdrawal transaction history
        transactionHistory.setAcntBlnc(acntBlnc - trnsAmt);
        createTransactionHistory(transactionHistory);

        TransactionResult transactionResult = TransactionResult.builder()
                .acntNo(acntNo)
                .seq(transactionHistory.getSeq())  // Store seq assigned when creating transaction history
                .formerBlnc(acntBlnc)
                .trnsAmt(trnsAmt)
                .acntBlnc(transactionHistory.getAcntBlnc())
                .build();

        return transactionResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public int processExternalTransferConfirmation(TransactionHistory transactionHistory) throws Exception {
        // 1) Update transaction history to withdrawal success status
        int result = accountRepository.updateTransactionHistory(transactionHistory);

        return result;
    }

    public int retrieveMaxSeq(String acntNo) throws Exception {
        return accountRepository.selectMaxSeq(TransactionHistory.ofAcntNo(acntNo));
    }
}