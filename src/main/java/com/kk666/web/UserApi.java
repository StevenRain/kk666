package com.kk666.web;


import com.kk666.dto.BalanceResponseDto;
import com.kk666.dto.WithdrawResponseDto;
import com.kk666.enums.GameIdEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("")
public interface UserApi {

    @ApiOperation(value = "批量登录", tags = "User")
    @GetMapping(path = "/batchLogin")
    ResponseEntity<String> batchLogin();

    @ApiOperation(value = "批量查询余额", tags = "User")
    @GetMapping(path = "/batchBalance", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<BalanceResponseDto>> batchBalance();

    @ApiOperation(value = "批量投注", tags = "Betting")
    @GetMapping(path = "/batchBetting")
    ResponseEntity<String> batchBetting(@ApiParam("彩种") GameIdEnum gameIdEnum, @ApiParam("单价") double price);

    @ApiOperation(value = "批量刷单", tags = "Betting")
    @GetMapping(path = "/batchBettingMock")
    ResponseEntity<String> batchBettingMock(@ApiParam("彩种") GameIdEnum gameIdEnum, @ApiParam("单价") double price);

    @ApiOperation(value = "批量查看出款记录", tags = "Withdraw")
    @GetMapping(path = "/batchWithdrawOrders", produces = "application/json; charset=UTF-8")
    ResponseEntity<List<WithdrawResponseDto>> batchWithdrawOrders();
}
