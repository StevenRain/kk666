package com.kk666.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawResponseDto {
    private String domain;
    private String username;
    private String time;
    private double amount;
    private String status;
}
