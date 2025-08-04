package com.monolithicbank.customer.repository;

import org.apache.ibatis.annotations.Mapper;

import com.monolithicbank.customer.entity.Customer;

@Mapper
public interface CustomerRepository {
    int insertCustomer(Customer customer) throws Exception;
    int deleteCustomer(Customer customer) throws Exception;
    Customer selectCustomer(Customer customer) throws Exception;
    int existsCustomer(Customer customer) throws Exception;
}