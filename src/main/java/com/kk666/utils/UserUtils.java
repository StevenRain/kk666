package com.kk666.utils;

import com.google.common.collect.Maps;
import com.google.gson.JsonParser;
import com.kk666.dto.UserDto;
import com.kk666.enums.GameIdEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
public class UserUtils {

    private UserUtils() {
    }

    public static void batchLogin() {
        List<UserDto> userDtoList = ConfigUtils.getAllUserDtoList();
        String urlPattern = "%s/apis/login";
        String jsonPattern = "{\"userName\":\"%s\",\"passWord\":\"%s\",\"otp\":\"12\"}";

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("fr", "9");

        userDtoList.forEach(userDto -> {
            log.info("正在登录 domain = {}, username = {}", userDto.getDomain(), userDto.getUsername());

            String url = String.format(urlPattern, userDto.getDomain());
            String payload = String.format(jsonPattern, userDto.getUsername(), userDto.getPassword());
            String response = HttpUtils.sendPostByJsonData(url, headerMap, payload);
            if(response.contains("token")) {
                String token = new JsonParser().parse(response).getAsJsonObject().get("data").getAsJsonObject().get("token").getAsString();
                userDto.setToken(token);
                ConfigUtils.updateToken(userDto);
                log.info("domain = {}, username = {} 登录成功", userDto.getDomain(), userDto.getUsername());
            }else {
                log.error("domain = {}, username = {} 登录失败, response = {}", userDto.getDomain(), userDto.getUsername(), response);
            }
        });
    }

    public static Double getBalance(UserDto userDto) {
        String pattern = "%s/apis/money/findBalanceApp";
        String url = String.format(pattern, userDto.getDomain());

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("fr", "9");
        headerMap.put("tk", userDto.getToken());

        String payload = "{}";

        String response = HttpUtils.sendPostByJsonData(url, headerMap, payload);
        log.info("正在查询 domain = {}, username = {} 的余额", userDto.getDomain(), userDto.getUsername());
        if(response.contains("balance")) {
            double balance = new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("data").get("balance").getAsDouble();
            log.info("domain = {}, username = {}, balance = {} 余额查询成功", userDto.getDomain(), userDto.getUsername(), balance);
            return balance;
        }else {
            log.error("domain = {}, username = {} 余额查询失败, response = {}", userDto.getDomain(), userDto.getUsername(), response);
        }
        return 0.0;
    }

    public static String getLatestIssueNo(UserDto userDto, GameIdEnum gameIdEnum) {
        String pattern = "%s/apis/lotIssue/findOpen?lotId=%d";
        String url = String.format(pattern, userDto.getDomain(), gameIdEnum.getGameId());

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("fr", "9");

        String response = HttpUtils.sendGet(url, headerMap);
        if(response.contains("issue")) {
            String gameIssueNo = new JsonParser().parse(response).getAsJsonObject().get("data").getAsJsonObject().get("issue").getAsString();
            log.info("domain = {}, gameName = {}, issueNo = {} 期号获取成功", userDto.getDomain(), gameIdEnum.getGameName(), gameIssueNo);
            return gameIssueNo;
        }else {
            log.error("domain = {}, gameName = {} 期号获取成功, response = {}", userDto.getDomain(), gameIdEnum.getGameName(), response);
        }
        return "";
    }

    public static void betting(UserDto userDto, GameIdEnum gameIdEnum, double price) {
        String urlPattern = "%s/apis/orderLot/addApp";
        String bettingPattern1 = "{\"lotId\":%d,\"isChase\":0,\"chaseCount\":0,\"baseInfo\":[{\"key\":\"sfsscqszuxfs\",\"playId\":471,\"betCode\":\"%d,%d\",\"betNum\":2,\"thisReward\":0,\"odds\":326.666,\"betType\":0,\"oneMoney\":\"%f\",\"money\":%f,\"position\":\"\",\"issue\":\"%s\"}]}";
        String bettingPattern2 = "{\"lotId\":%d,\"isChase\":0,\"chaseCount\":0,\"baseInfo\":[{\"key\":\"sfsschszuxfs\",\"playId\":442,\"betCode\":\"%d,%d\",\"betNum\":2,\"thisReward\":0,\"odds\":326.666,\"betType\":0,\"oneMoney\":\"%f\",\"money\":%f,\"position\":\"\",\"issue\":\"%s\"}]}";
        String url = String.format(urlPattern, userDto.getDomain());
        String issueNo = getLatestIssueNo(userDto, gameIdEnum);

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("fr", "9");
        headerMap.put("tk", userDto.getToken());

        IntStream.rangeClosed(0, 9).parallel().forEach(index -> {
            String bettingData1 = String.format(bettingPattern1, gameIdEnum.getGameId(), index, index, price, price * 2, issueNo);
            String bettingData2 = String.format(bettingPattern2, gameIdEnum.getGameId(), index, index, price, price * 2, issueNo);

            log.info("正在投注 game = {}, issue = {}, playId = 前三-组三复式, domain = {}, username = {}, price = {}", gameIdEnum.getGameName(),issueNo, userDto.getDomain(), userDto.getUsername(), price);
            String response1 = HttpUtils.sendPostByJsonData(url, headerMap, bettingData1);
            log.info(response1);
            if(response1.contains("200")) {
                log.info("game = {}, issue = {}, playId = 前三-组三复式, domain = {}, username = {}, price = {} 投注成功", gameIdEnum.getGameName(), issueNo, userDto.getDomain(), userDto.getUsername(), price);
            }else {
                log.error("game = {}, issue = {}, playId = 前三-组三复式, domain = {}, username = {}, price = {} 投注失败, response = {}", gameIdEnum.getGameName(), issueNo, userDto.getDomain(), userDto.getUsername(), price, response1);
            }

            log.info("正在投注 game = {}, issue = {}, playId = 后三-组三复式, domain = {}, username = {}, price = {}", gameIdEnum.getGameName(), issueNo, userDto.getDomain(), userDto.getUsername(), price);
            String response2 = HttpUtils.sendPostByJsonData(url, headerMap, bettingData2);
            log.info(response2);
            if(response2.contains("200")) {
                log.info("game = {}, issue = {}, playId = 后三-组三复式, domain = {}, username = {}, price = {} 投注成功", gameIdEnum.getGameName(), issueNo, userDto.getDomain(), userDto.getUsername(), price);
            }else {
                log.error("game = {}, issue = {}, playId = 后三-组三复式, domain = {}, username = {}, price = {} 投注失败, response = {}", gameIdEnum.getGameName(), issueNo, userDto.getDomain(), userDto.getUsername(), price, response2);
            }
        });
    }
}
