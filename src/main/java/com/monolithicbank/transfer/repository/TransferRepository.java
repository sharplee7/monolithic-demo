package com.monolithicbank.transfer.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.monolithicbank.transfer.entity.TransferHistory;
import com.monolithicbank.transfer.entity.TransferLimit;

@Mapper
public interface TransferRepository {
    int insertTransferHistory(TransferHistory transferHistory) throws Exception;
    List<TransferHistory> selectTransferHistoryList(TransferHistory transferHistory) throws Exception;
    int insertTransferLimit(TransferLimit transferLimit) throws Exception;
    TransferLimit selectTransferLimit(TransferLimit transferLimit) throws Exception;
    Long selectTotalTransferAmountPerDay(TransferLimit transferLimit) throws Exception;
    int selectMaxSeq(TransferHistory transferHistory) throws Exception;
}