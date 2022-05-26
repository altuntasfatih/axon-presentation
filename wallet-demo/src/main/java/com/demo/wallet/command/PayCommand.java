package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Getter
public class PayCommand {

    @TargetAggregateIdentifier
    private final String walletId;//to find aggregate it is mandatory
    private final BigDecimal payAmount;

    public PayCommand(String walletId, BigDecimal payAmount) {
        this.walletId = walletId;
        this.payAmount = payAmount;
    }
}
