package com.monolithicbank.b2bt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monolithicbank.account.entity.TransactionHistory;
import com.monolithicbank.account.service.AccountService;
import com.monolithicbank.b2bt.entity.B2BTransferHistory;
import com.monolithicbank.transfer.entity.TransferHistory;

/**
 * B2B Transfer Service - handles inter-bank transfers
 * In monolithic architecture, this simulates external bank transfer processing
 */
@Service("b2bTransferService")
public class B2BTransferService {
    
    private static final Logger logger = LoggerFactory.getLogger(B2BTransferService.class);
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Process inter-bank transfer
     * In real scenario, this would communicate with external bank systems
     * For demo purposes, we simulate the process and confirm the transfer
     */
    public void processTransfer(TransferHistory transferHistory) throws Exception {
        logger.info("Processing B2B transfer for customer: {}, amount: {}", 
                   transferHistory.getCstmId(), transferHistory.getTrnfAmt());
        
        // Convert to B2B transfer history
        B2BTransferHistory b2bTransfer = new B2BTransferHistory();
        b2bTransfer.setCstmId(transferHistory.getCstmId());
        b2bTransfer.setSeq(transferHistory.getSeq());
        b2bTransfer.setDivCd(transferHistory.getDivCd());
        b2bTransfer.setStsCd(transferHistory.getStsCd());
        b2bTransfer.setDpstAcntNo(transferHistory.getDpstAcntNo());
        b2bTransfer.setWthdAcntNo(transferHistory.getWthdAcntNo());
        b2bTransfer.setWthdAcntSeq(transferHistory.getWthdAcntSeq());
        b2bTransfer.setSndMm(transferHistory.getSndMm());
        b2bTransfer.setRcvMm(transferHistory.getRcvMm());
        b2bTransfer.setRcvCstmNm(transferHistory.getRcvCstmNm());
        b2bTransfer.setTrnfAmt(transferHistory.getTrnfAmt());
        b2bTransfer.setTrnfDtm(transferHistory.getTrnfDtm());
        
        // Simulate external bank processing
        // In real scenario, this would involve:
        // 1. Send transfer request to external bank
        // 2. Wait for confirmation
        // 3. Handle success/failure response
        
        try {
            // Simulate processing time
            Thread.sleep(100);
            
            // For demo purposes, assume transfer is successful
            boolean transferSuccess = simulateExternalBankTransfer(b2bTransfer);
            
            if (transferSuccess) {
                // Confirm withdrawal success
                confirmTransferResult(transferHistory, "1"); // Success
                logger.info("B2B transfer completed successfully for customer: {}", transferHistory.getCstmId());
            } else {
                // Handle transfer failure - rollback withdrawal
                confirmTransferResult(transferHistory, "2"); // Failure
                logger.warn("B2B transfer failed for customer: {}", transferHistory.getCstmId());
            }
            
        } catch (Exception e) {
            logger.error("Error processing B2B transfer", e);
            // Handle error - rollback withdrawal
            confirmTransferResult(transferHistory, "2"); // Failure
            throw e;
        }
    }
    
    /**
     * Simulate external bank transfer processing
     * In real implementation, this would communicate with external systems
     */
    private boolean simulateExternalBankTransfer(B2BTransferHistory transfer) {
        // Simulate 95% success rate for demo purposes
        return Math.random() > 0.05;
    }
    
    /**
     * Confirm transfer result back to account service
     */
    private void confirmTransferResult(TransferHistory transferHistory, String resultStatus) throws Exception {
        TransactionHistory confirmTransaction = TransactionHistory.builder()
            .acntNo(transferHistory.getWthdAcntNo())
            .seq(transferHistory.getWthdAcntSeq())
            .stsCd(resultStatus) // "1" for success, "2" for failure
            .build();
            
        accountService.processExternalTransferConfirmation(confirmTransaction);
    }
}