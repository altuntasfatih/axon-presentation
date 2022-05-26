package com.demo.wallet.command;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RollBackMoneyTransferCommand extends BaseCommand<String> {

    private final String requestId;
    private final BigDecimal amount;

    public RollBackMoneyTransferCommand(String walletId, String requestId, BigDecimal amount) {
        super(walletId);
        this.requestId = requestId;
        this.amount = amount;
    }
}
