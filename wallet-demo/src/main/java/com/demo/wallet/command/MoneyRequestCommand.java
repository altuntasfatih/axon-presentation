package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Getter
public class MoneyRequestCommand {
    @TargetAggregateIdentifier
    private final String walletId;
    private final String fromWalletId;
    private final BigDecimal amount;

    public MoneyRequestCommand(String walletId, String fromWalletId, BigDecimal amount) {
        this.walletId = walletId;
        this.fromWalletId = fromWalletId;
        this.amount = amount;
    }
}
