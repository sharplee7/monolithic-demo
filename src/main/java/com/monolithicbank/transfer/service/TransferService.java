package com.monolithicbank.transfer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monolithicbank.account.entity.Account;
import com.monolithicbank.account.entity.TransactionHistory;
import com.monolithicbank.account.entity.TransactionResult;
import com.monolithicbank.account.service.AccountService;
import com.monolithicbank.b2bt.service.B2BTransferService;
import com.monolithicbank.common.exception.BusinessException;
import com.monolithicbank.common.exception.SystemException;
import com.monolithicbank.customer.service.CustomerService;
import com.monolithicbank.transfer.entity.TransferHistory;
import com.monolithicbank.transfer.entity.TransferLimit;
import com.monolithicbank.transfer.repository.TransferRepository;

@Service("transferService")
public class TransferService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
	
    @Autowired
    TransferRepository transferRepository;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    CustomerService customerService;
    
    @Autowired
    B2BTransferService b2bTransferService;

    public int createTransferHistory(TransferHistory transferHistory) throws Exception {
    	return transferRepository.insertTransferHistory(transferHistory);
    }

    public List<TransferHistory> retrieveTransferHistoryList(String cstmId) throws Exception {
        Boolean exists = customerService.existsCustomerId(cstmId);

        if (!exists) {
            throw new BusinessException("ID does not exist.");
        }
        
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setCstmId(cstmId);
        
        return transferRepository.selectTransferHistoryList(transferHistory);
    }

    @Transactional(rollbackFor = Exception.class)
    public int createTransferLimit(TransferLimit transferLimit) throws Exception {
    	return transferRepository.insertTransferLimit(transferLimit);
    }

    public TransferLimit retrieveTransferLimit(String cstmId) throws Exception {
    	TransferLimit transferLimit = new TransferLimit();
    	transferLimit.setCstmId(cstmId);
    	return transferRepository.selectTransferLimit(transferLimit);
    }

    public Long retrieveTotalTransferAmountPerDay(String cstmId) throws Exception {
    	TransferLimit transferLimit = new TransferLimit();
    	transferLimit.setCstmId(cstmId);
        return transferRepository.selectTotalTransferAmountPerDay(transferLimit);
    }
    
	public TransferLimit retrieveEnableTransferLimit(String cstmId) throws Exception {
		TransferLimit transferLimit = retrieveTransferLimit(cstmId);
        if(transferLimit == null)
            throw new BusinessException("Failed to retrieve transfer limit.");
        else {
            Long totalTransferAmountPerDay = retrieveTotalTransferAmountPerDay(cstmId);
            if(totalTransferAmountPerDay < 0)
                throw new BusinessException("Failed to retrieve total transfer amount per day.");
            else {
                Long remaingOneDayTransferLimit = transferLimit.getOneDyTrnfLmt() - totalTransferAmountPerDay;
                transferLimit.setOneDyTrnfLmt(remaingOneDayTransferLimit);
                return transferLimit;
            }
        }
	}

    @Transactional(rollbackFor = Exception.class)
    public TransferHistory transfer(TransferHistory transferHistory) throws Exception {
        String dpstAcntNo = transferHistory.getDpstAcntNo();
        String rcvCstmNm;
        String wthdAcntNo = transferHistory.getWthdAcntNo();
        Long trnfAmt = transferHistory.getTrnfAmt();
        String rcvMm = transferHistory.getRcvMm();
        String sndMm = transferHistory.getSndMm();
        String cstmId = transferHistory.getCstmId();
        int seq = retrieveMaxSeq(cstmId) + 1;
        
        Account depositAccountInfo = accountService.retrieveAccount(dpstAcntNo);
        rcvCstmNm = depositAccountInfo.getCstmNm();
        
        transferHistory.setRcvCstmNm(rcvCstmNm);
        transferHistory.setSeq(seq);
        transferHistory.setDivCd("D");
        transferHistory.setStsCd("1");
        createTransferHistory(transferHistory);
        
        // 내부 이체의 경우 '0' 즉, 팬딩 처리 없이 바로 출금 성공 처리(1)를 한다.
        performWithdrawal(wthdAcntNo, trnfAmt, sndMm, transferHistory.getDivCd(), transferHistory.getStsCd());

        performDeposit(dpstAcntNo, trnfAmt, rcvMm);

        transferHistory.setStsCd("3");
        createTransferHistory(transferHistory);
            
        TransferHistory transferResult = new TransferHistory();
        transferResult.setWthdAcntNo(wthdAcntNo);
        transferResult.setDpstAcntNo(dpstAcntNo);
        transferResult.setRcvCstmNm(rcvCstmNm);
        transferResult.setTrnfAmt(trnfAmt);
        transferResult.setRcvMm(rcvMm);
        transferResult.setSndMm(sndMm);
        
        return transferResult;
    }

    private TransactionResult performWithdrawal(String acntNo, Long amount, String branch, String divCd, String stsCd) throws Exception {
        TransactionHistory.TransactionHistoryBuilder builder = TransactionHistory.builder()
        .acntNo(acntNo)
        .trnsAmt(amount)
        .trnsBrnch(branch);

        if (divCd != null) {
            builder.divCd(divCd);
        }
        if (stsCd != null) {
            builder.stsCd(stsCd);
        }

        TransactionHistory transaction = builder.build();
        
        return accountService.withdrawOwnBankOrTransferOtherBank(transaction);
    }

    private void performDeposit(String acntNo, Long amount, String branch) throws Exception {
        TransactionHistory transaction = TransactionHistory.builder()
            .acntNo(acntNo)
            .trnsAmt(amount)
            .trnsBrnch(branch)
            .build();
        accountService.deposit(transaction);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean interBankTransfer(TransferHistory transfer) throws Exception {
        String wthdAcntNo = transfer.getWthdAcntNo();
        Long trnfAmt = transfer.getTrnfAmt();
        String sndMm = transfer.getSndMm();
        transfer.setRcvCstmNm("Amazon Web Services");
        String cstmId = transfer.getCstmId();
        int seq = retrieveMaxSeq(cstmId) + 1;
        
        transfer.setSeq(seq);
        transfer.setDivCd("W");        
        // TB_TRNF_HST 테이블에 이체 이력 남기기
        createTransferHistory(transfer);
        
        // Account Service에 고객 계좌에서 타행 이체 금액 인출
        TransactionResult withdrawResult = performWithdrawal(wthdAcntNo, trnfAmt, sndMm, transfer.getDivCd(), transfer.getStsCd());
        
        if (withdrawResult == null) {
            throw new SystemException("Failed to receive withdrawal result.");
        }
        
        int wthdAcntSeq = withdrawResult.getSeq();
        transfer.setWthdAcntSeq(wthdAcntSeq);
        
        // B2B Transfer 서비스 호출
        b2bTransferService.processTransfer(transfer);
            
        return true;
    }

    private int retrieveMaxSeq(String cstmId) throws Exception {
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setCstmId(cstmId);
        return transferRepository.selectMaxSeq(transferHistory);
    }
}