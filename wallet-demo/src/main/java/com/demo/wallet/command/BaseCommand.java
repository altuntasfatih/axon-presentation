package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
public class BaseCommand<T> {

    @TargetAggregateIdentifier
    private final T walletId;

    public BaseCommand(T walletId) {
        this.walletId = walletId;
    }
}

