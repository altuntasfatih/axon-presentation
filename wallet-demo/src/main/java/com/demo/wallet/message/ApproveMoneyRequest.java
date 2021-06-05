package com.demo.wallet.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApproveMoneyRequest {
    private String requestId;
    private String fromId;
    private BigDecimal amount;
}
