package com.monolithicbank.transfer.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monolithicbank.transfer.entity.TransferHistory;
import com.monolithicbank.transfer.entity.TransferLimit;
import com.monolithicbank.transfer.service.TransferService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

@RestController
@RequestMapping("/transfer")
@Api(tags = "Transfer", description = "이체 관리 API")
public class TransferController {
    private final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    @Resource(name = "transferService")
    private TransferService transferService;
    
    @ApiOperation(value = "내부 이체", notes = "동일 은행 내 계좌 간 이체를 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @PostMapping("/internal")
    public TransferHistory transfer(
            @ApiParam(value = "이체 정보", required = true)
            @RequestBody TransferHistory input) throws Exception{
        LOGGER.info("--> call internal transfer ");
        return transferService.transfer(input);
    }
    
    // Inter-bank transfer
    @ApiOperation(value = "외부 이체", notes = "다른 은행으로의 이체를 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @PostMapping("/external")
    public Boolean btobTransfer(
            @ApiParam(value = "외부 이체 정보", required = true)
            @RequestBody TransferHistory input) throws Exception{
        LOGGER.info("=====>DivCd: " + input.getDivCd() + ", StsCd:" + input.getStsCd());

        return transferService.interBankTransfer(input);
    }

    @ApiOperation(value = "이체 내역 조회", notes = "고객ID로 이체 내역을 조회합니다.")
    @GetMapping("/history/{cstmId}")
    public List<TransferHistory> retrieveTransferHistoryList(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception{
        List<TransferHistory> transferHistory = transferService.retrieveTransferHistoryList(cstmId);
        return transferHistory;
    }

    @ApiOperation(value = "이체 한도 설정", notes = "고객의 이체 한도를 설정합니다.")
    @PostMapping("/limits")
    public Integer createTransferLimit(
            @ApiParam(value = "이체 한도 정보", required = true)
            @RequestBody TransferLimit input) throws Exception{
        return  transferService.createTransferLimit(input);
    }

    @ApiOperation(value = "이체 한도 조회", notes = "고객ID로 이체 한도를 조회합니다.")
    @GetMapping("/limits/{cstmId}")
    public TransferLimit retrieveTransferLimit(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception{
        return transferService.retrieveTransferLimit(cstmId);
    }

    @ApiOperation(value = "사용 가능 이체 한도 조회", notes = "고객ID로 사용 가능한 이체 한도를 조회합니다.")
    @GetMapping("/limits/{cstmId}/available")
    public TransferLimit retrieveEnableTransferLimit(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception{
        return  transferService.retrieveEnableTransferLimit(cstmId);
    }
}