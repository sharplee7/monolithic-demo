package com.monolithicbank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monolithicbank.account.entity.Account;
import com.monolithicbank.account.entity.TransactionHistory;
import com.monolithicbank.account.entity.TransactionResult;
import com.monolithicbank.account.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account")
@Api(tags = "Account", description = "계좌 관리 API")
public class AccountController {

    @Autowired
    @Resource(name = "accountService")
    private AccountService accountService;
    
    @ApiOperation(value = "계좌 조회", notes = "계좌번호로 계좌 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "계좌를 찾을 수 없음")
    })
    @GetMapping("/{acntNo}")
    public Account retrieveAccount(
            @ApiParam(value = "계좌번호", required = true) 
            @PathVariable(name = "acntNo") String acntNo) throws Exception {
        return accountService.retrieveAccount(acntNo);
    }

    @ApiOperation(value = "계좌 생성", notes = "새로운 계좌를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @PostMapping("/")
    public Integer createAccount(
            @ApiParam(value = "계좌 정보", required = true)
            @RequestBody Account account) throws Exception {
        return accountService.createAccount(account);
    }

    @ApiOperation(value = "고객 계좌 목록 조회", notes = "고객ID로 해당 고객의 모든 계좌를 조회합니다.")
    @GetMapping("/customer/{cstmId}/accounts")
    public List<Account> retrieveAccountList(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception {
        return accountService.retrieveAccountList(cstmId);
    }

    @ApiOperation(value = "계좌 잔액 조회", notes = "계좌번호로 계좌 잔액을 조회합니다.")
    @GetMapping("/{acntNo}/balance")
    public Long retrieveAccountBalance(
            @ApiParam(value = "계좌번호", required = true)
            @PathVariable(name = "acntNo") String acntNo) throws Exception {
        return accountService.retrieveAccountBalance(acntNo);
    }

    @ApiOperation(value = "입금", notes = "계좌에 입금을 처리합니다.")
    @PostMapping("/deposits/")
    public TransactionResult deposit(
            @ApiParam(value = "입금 정보", required = true)
            @RequestBody TransactionHistory input) throws Exception {
        try {
            System.out.println("==> AccountController.deposit() - Account: " + input.getAcntNo() + ", Amount: " + input.getTrnsAmt());
            return accountService.deposit(input);
        } catch (Exception e) {
            System.err.println("Error in deposit: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @ApiOperation(value = "출금/이체", notes = "계좌에서 출금하거나 다른 계좌로 이체를 처리합니다.")
    @PostMapping("/withdrawals/")
    public TransactionResult withdrawal(
            @ApiParam(value = "출금/이체 정보", required = true)
            @RequestBody TransactionHistory input) throws Exception {
        System.out.println("==> AccountController.withdrawal()");
        System.out.println("DivCD: " + input.getDivCd() + ", StatusCD: " + input.getStsCd()); 
        return accountService.withdrawOwnBankOrTransferOtherBank(input);
    }

    @ApiOperation(value = "외부 이체 확인", notes = "외부 이체 결과를 확인하고 처리합니다.")
    @PostMapping("/withdrawals/confirm/")
    public Integer processExternalTransferConfirmation(
            @ApiParam(value = "이체 확인 정보", required = true)
            @RequestBody TransactionHistory input) throws Exception {
        return accountService.processExternalTransferConfirmation(input);
    }
    
    @ApiOperation(value = "거래 내역 조회", notes = "계좌번호로 거래 내역을 조회합니다.")
    @GetMapping("/{acntNo}/transactions")
    public List<TransactionHistory> retrieveTransactionHistory(
            @ApiParam(value = "계좌번호", required = true)
            @PathVariable(name = "acntNo") String acntNo) throws Exception {
        return accountService.retrieveTransactionHistoryList(acntNo);
    }
}