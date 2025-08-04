package com.monolithicbank.account.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.monolithicbank.account.entity.Account;
import com.monolithicbank.account.entity.TransactionHistory;

@Mapper
public interface AccountRepository {
    int insertAccount(Account account) throws Exception;
    Account selectAccount(Account account) throws Exception;
    List<Account> selectAccountList(Account account) throws Exception;
    int insertTransactionHistoryData(TransactionHistory transactionHistory) throws Exception;
    List<TransactionHistory> selectTransactionHistoryList(TransactionHistory transactionHistory) throws Exception;
    Long selectCurrentAccountBalance(TransactionHistory transactionHistory) throws Exception;
    int updateTransactionHistory(TransactionHistory transactionHistory) throws Exception;
    int selectMaxSeq(TransactionHistory transactionHistory) throws Exception;
}