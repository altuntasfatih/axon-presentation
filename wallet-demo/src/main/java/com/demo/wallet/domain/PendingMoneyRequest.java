package com.demo.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingMoneyRequest {
    private String requestId;
    private String fromId;
    private BigDecimal amount;
}
