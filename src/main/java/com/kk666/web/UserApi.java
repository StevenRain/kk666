package com.kk666.web;


import com.kk666.enums.GameIdEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("")
public interface UserApi {

    @ApiOperation(value = "批量登录", tags = "User")
    @GetMapping(path = "/batchLogin")
    ResponseEntity<String> batchLogin();

    @ApiOperation(value = "批量查询余额", tags = "User")
    @GetMapping(path = "/batchBalance")
    ResponseEntity<String> batchBalance();

    @ApiOperation(value = "批量投注", tags = "Betting")
    @GetMapping(path = "/batchBetting")
    ResponseEntity<String> batchBetting(@ApiParam("彩种") GameIdEnum gameIdEnum, @ApiParam("单价") double price);
}
