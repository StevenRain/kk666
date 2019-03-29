package com.kk666.web;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kk666.dto.BalanceResponseDto;
import com.kk666.dto.UserDto;
import com.kk666.dto.WithdrawResponseDto;
import com.kk666.enums.GameIdEnum;
import com.kk666.utils.ConfigUtils;
import com.kk666.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserApiController implements UserApi {

    @Override
    public ResponseEntity<String> batchLogin() {
        UserUtils.batchLogin();
        return ResponseEntity.ok("登录成功,token已自动更新到config.txt");
    }


    @Override
    public ResponseEntity<List<BalanceResponseDto>> batchBalance() {
        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();
        List<BalanceResponseDto> balanceResponseDtoList = userDtoList.parallelStream().map(userDto -> {
            double balance = UserUtils.getBalance(userDto);
            return BalanceResponseDto.builder()
                    .domain(userDto.getDomain())
                    .username(userDto.getUsername())
                    .balance(balance)
                    .build();
        }).collect(Collectors.toList());
        return ResponseEntity.ok(balanceResponseDtoList);
    }

    @Override
    public ResponseEntity<String> batchBetting(GameIdEnum gameIdEnum, double price) {
        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();
        userDtoList.parallelStream().forEach(userDto -> UserUtils.betting(userDto, gameIdEnum, price));
        return ResponseEntity.ok("投注完成，投注结果请看日志!");
    }

    @Override
    public ResponseEntity<String> batchBettingMock(GameIdEnum gameIdEnum, double price) {
        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();
        userDtoList.parallelStream().forEach(userDto -> UserUtils.bettingMock(userDto, gameIdEnum, price));
        return ResponseEntity.ok("本次刷单完成，刷单结果请看日志!");
    }

    @Override
    public ResponseEntity<List<WithdrawResponseDto>> batchWithdrawOrders() {
        UnaryOperator<String> function = status -> {
            if(status.equals("0"))
                return "审核中";
            if(status.equals("5"))
                return "提款成功";
            if(status.equals("6"))
                return "已取消";
            return "未知";
        };

        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();

        List<WithdrawResponseDto> withdrawResponseDtoList = userDtoList.parallelStream().map(userDto -> {
            String response = UserUtils.getWithdrawOrder(userDto);
            if(response.contains("resultList")) {
                JsonObject recordJson = new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("data").getAsJsonArray("resultList").iterator().next().getAsJsonObject();
                String createTime = recordJson.get("createTime").getAsString();
                double amount = recordJson.get("infactMoney").getAsDouble();
                String status = recordJson.get("status").getAsString();
                status = function.apply(status);
                return WithdrawResponseDto.builder().time(createTime).amount(amount).status(status).domain(userDto.getDomain()).username(userDto.getUsername()).build();
            }else {
                return WithdrawResponseDto.builder().status("查询失败").domain(userDto.getDomain()).username(userDto.getUsername()).build();
            }
        }).collect(Collectors.toList());
        return ResponseEntity.ok(withdrawResponseDtoList);
    }
}
