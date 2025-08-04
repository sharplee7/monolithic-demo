package com.monolithicbank.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monolithicbank.customer.entity.Customer;
import com.monolithicbank.customer.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer")
@Api(tags = "Customer", description = "고객 관리 API")
public class CustomerController {

    @Resource(name = "customerService")
    private CustomerService customerService;
    
    @ApiOperation(value = "고객 등록", notes = "새로운 고객을 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청")
    })
    @PostMapping("/")
    public Integer createCustomer(
            @ApiParam(value = "고객 정보", required = true)
            @RequestBody Customer customer) throws Exception{
        System.out.println("-----> 1.Controller request...");
         return customerService.createCustomer(customer);
    }

    @ApiOperation(value = "고객 조회", notes = "고객ID로 고객 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "고객을 찾을 수 없음")
    })
    @GetMapping("/{cstmId}")
    public Customer retrieveCustomer(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception{
        return customerService.retrieveCustomer(cstmId);
    }

    @ApiOperation(value = "고객 상세 조회", notes = "고객ID로 고객 상세 정보를 조회합니다.")
    @GetMapping("/{cstmId}/details")
    public Customer retrieveCustomerDetail(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception{
        return customerService.retrieveCustomerDetail(cstmId);
    }

    @ApiOperation(value = "고객 존재 확인", notes = "고객ID가 존재하는지 확인합니다.")
    @GetMapping("/{cstmId}/exists")
    public Boolean existsCustomerId(
            @ApiParam(value = "고객ID", required = true)
            @PathVariable(name = "cstmId") String cstmId) throws Exception{
    	return customerService.existsCustomerId(cstmId);
    }
}