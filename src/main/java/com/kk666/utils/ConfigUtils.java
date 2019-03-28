package com.kk666.utils;

import com.google.common.collect.Lists;
import com.kk666.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConfigUtils {

    private static final String FILE_NAME = "config.txt";

    private ConfigUtils() {
    }

    private static List<String> getAllLines() {
        String dir = System.getProperty("user.dir");
        Path path = Paths.get(dir + "/" + FILE_NAME);
        try {
            return Files.readAllLines(path);
        }catch (Exception e) {
            log.error("{}", e);
        }
        return Lists.newArrayList();
    }

    public static List<UserDto> getAllUserDtoList() {
        List<String> lines = getAllLines();
        return lines.stream().filter(line -> !line.startsWith("#")).map(line -> {
            String[] array = line.split(",");
            UserDto userDto = UserDto.builder()
                    .domain(array[0])
                    .username(array[1])
                    .password(array[2])
                    .build();
            if(array.length > 3) {
                userDto.setToken(array[3]);
            }
            return userDto;
        }).collect(Collectors.toList());
    }

    private static void writeLines(List<String> lines) {
        String dir = System.getProperty("user.dir");
        try {
            FileWriter writer = new FileWriter(dir + "/" + FILE_NAME);
            lines.forEach(line -> {
                try {
                    writer.append(line);
                    writer.append("\n");
                }catch (Exception e) {
                    log.error("{}", e);
                }
            });
            writer.close();
        }catch (Exception e) {
            log.error("{}", e);
        }
    }

    public static void updateToken(UserDto userDto) {
        List<String> lines = getAllLines();
        List<String> newLines = lines.stream().map(line -> {
            if(line.startsWith(userDto.getDomain()) && line.contains(userDto.getUsername())) {
                return userDto.getDomain() + "," + userDto.getUsername() + "," + userDto.getPassword() + "," + userDto.getToken();
            }
            return line;
        }).collect(Collectors.toList());

        writeLines(newLines);
    }
}
