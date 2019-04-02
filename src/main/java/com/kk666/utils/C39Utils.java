package com.kk666.utils;

import com.google.common.collect.Maps;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class C39Utils {

    private static String buildSign(Map<String, String> parameterMap) {
        StringBuilder builder = new StringBuilder("lottery@alpha");
        parameterMap.remove("sign");
        parameterMap.forEach((key, value) -> builder.append(key).append(value));
        return MD5Utils.encode(builder.toString());
    }

    public static double getBalance() {
        String timestamp = System.currentTimeMillis() + "";
        Map<String, String> parameterMap = Maps.newTreeMap();
        parameterMap.put("token", "A76315262F91031C31215344A66A7892");
        parameterMap.put("userId", "939774");

        parameterMap.put("timestamp", timestamp);
        parameterMap.put("sign", buildSign(parameterMap));
        StringBuilder builder = new StringBuilder();
        parameterMap.forEach((key, value) -> builder.append("&").append(key).append("=").append(value));
        String urlPrefix = "https://www.222c39.com/mobile/02/001";
        String url = urlPrefix + builder.toString().replaceFirst("&", "?");
        String response = HttpUtils.sendGet(url, Maps.newHashMap());
        if(response.contains("balance")) {
            return new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("user").get("balance").getAsDouble();
        }
        return 0.0;
    }

    public static String betting() {
        Map<String, String> parameterMap = Maps.newTreeMap();
        parameterMap.put("token", "A76315262F91031C31215344A66A7892");
        parameterMap.put("timestamp", System.currentTimeMillis() + "");
        parameterMap.put("userId", "939774");
        parameterMap.put("lotteryId", "20");
        parameterMap.put("lotteryNo", "20190402025");
        parameterMap.put("playId", "915");
        parameterMap.put("playDetailId", "600");
        parameterMap.put("bettingValue", "1 2 02");
        parameterMap.put("odds", "31.68");
        parameterMap.put("rebate", "0.0");
        parameterMap.put("num", "1");
        parameterMap.put("amount", "2");
        parameterMap.put("addPeriods", "1");
        parameterMap.put("version", "8.8.8");
        parameterMap.put("addPeriodsStop", "0");
        parameterMap.put("addPeriodsNos", "20190402025");
        parameterMap.put("addPeriodsTimes", "1");
        parameterMap.put("sign", "A939C5B3F586E378129A6662693338BF");

        String urlPrefix = "https://www.222c39.com/mobile/09/001";
        parameterMap.put("sign", buildSign(parameterMap));
        String response = HttpUtils.sendPostByFormData(urlPrefix, Maps.newHashMap(), parameterMap);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) {
        betting();
    }
}
