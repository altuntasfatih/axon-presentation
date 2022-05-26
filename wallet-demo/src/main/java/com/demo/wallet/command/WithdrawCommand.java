package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateVersion;

import java.math.BigDecimal;

@Getter
public class WithdrawCommand {

    //to find aggregate it is mandatory
    @TargetAggregateIdentifier
    private final String walletId;

    /*
     @TargetAggregateVersion is used to indicate the expected version of the Aggregate.
    */
    @TargetAggregateVersion
    private final Long version;

    private final BigDecimal withdrawAmount;

    public WithdrawCommand(String walletId, Long version, BigDecimal withdrawAmount) {
        this.walletId = walletId;
        this.version = version;
        this.withdrawAmount = withdrawAmount;
    }
}
