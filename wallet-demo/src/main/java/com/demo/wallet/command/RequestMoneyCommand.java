package com.demo.wallet.command;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RequestMoneyCommand extends BaseCommand<String> {

    private final String requestId;
    private final String fromId;
    private final BigDecimal amount;

    public RequestMoneyCommand(String walletId, String requestId, String fromId, BigDecimal amount) {
        super(walletId);
        this.requestId = requestId;
        this.fromId = fromId;
        this.amount = amount;
    }
}
