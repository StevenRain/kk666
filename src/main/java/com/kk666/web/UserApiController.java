package com.kk666.web;

import com.kk666.dto.UserDto;
import com.kk666.enums.GameIdEnum;
import com.kk666.utils.ConfigUtils;
import com.kk666.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserApiController implements UserApi {

    @Override
    public ResponseEntity<String> batchLogin() {
        UserUtils.batchLogin();
        return ResponseEntity.ok("登录成功,token已自动更新到config.txt");
    }

    @Override
    public ResponseEntity<String> batchBalance() {
        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();
        StringBuilder builder = new StringBuilder();
        String pattern = "domain = %s, username = %s, balance = %f\n";
        userDtoList.forEach(userDto -> {
            double balance = UserUtils.getBalance(userDto);
            builder.append(String.format(pattern, userDto.getDomain(), userDto.getUsername(), balance));
        });
        return ResponseEntity.ok(builder.toString());
    }

    @Override
    public ResponseEntity<String> batchBetting(GameIdEnum gameIdEnum, double price) {
        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();
        userDtoList.parallelStream().forEach(userDto -> UserUtils.betting(userDto, gameIdEnum, price));
        return ResponseEntity.ok("投注完成，投注结果请看日志!");
    }
}
