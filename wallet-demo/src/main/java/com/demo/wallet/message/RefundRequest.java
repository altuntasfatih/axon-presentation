package com.demo.wallet.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefundRequest {
    private String orderId;
    private BigDecimal refundAmount;
}
