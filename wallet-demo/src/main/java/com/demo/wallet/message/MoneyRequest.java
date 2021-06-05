package com.demo.wallet.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoneyRequest {
    private String requestId;
    private String fromId;
    private BigDecimal amount;
}
