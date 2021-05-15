package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Getter
public class CreateWalletCommand {
    @TargetAggregateIdentifier
    private final String walletId = UUID.randomUUID().toString();
}
