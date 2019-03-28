package com.kk666.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepUtils {

    public SleepUtils() {
    }

    public static void sleep(long timeMilliSeconds) {
        try {
            Thread.sleep(timeMilliSeconds);
        }catch (Exception e) {
            log.error("{}", e);
        }
    }
}
