package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Getter
public class RefundCommand {

    @TargetAggregateIdentifier
    private final String walletId;//to find aggregate it is mandatory
    private final String orderId;
    private final BigDecimal refundAmount;

    public RefundCommand(String walletId, String orderId, BigDecimal refundAmount) {
        this.walletId = walletId;
        this.orderId = orderId;
        this.refundAmount = refundAmount;
    }
}
