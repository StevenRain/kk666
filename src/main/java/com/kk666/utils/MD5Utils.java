package com.kk666.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@UtilityClass
@Slf4j
public class MD5Utils {

    public static String encode(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        }catch (Exception e) {
            log.error("{}", e);
            return "";
        }
    }
}
