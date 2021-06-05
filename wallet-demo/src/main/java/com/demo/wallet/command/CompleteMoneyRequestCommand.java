package com.demo.wallet.command;

import lombok.Getter;

@Getter
public class CompleteMoneyRequestCommand extends BaseCommand<String> {

    private final String requestId;

    public CompleteMoneyRequestCommand(String walletId, String requestId) {
        super(walletId);
        this.requestId = requestId;
    }
}
