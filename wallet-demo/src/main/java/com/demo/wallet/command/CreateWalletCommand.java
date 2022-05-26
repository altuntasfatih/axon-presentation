package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
public class CreateWalletCommand {
    @TargetAggregateIdentifier
    private final String walletId;

    public CreateWalletCommand(String walletId) {
        this.walletId = walletId;
    }
}
