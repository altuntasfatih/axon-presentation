package com.demo.wallet.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateVersion;

@Getter
public class ChangePhoneNumberCommand {

    /*to find aggregate it is mandatory */
    @TargetAggregateIdentifier
    private final String walletId;

    /* @TargetAggregateVersion is used to indicate the expected version of the Aggregate.*/
    @TargetAggregateVersion
    private final Long version;

    private final String phoneNumber;

    public ChangePhoneNumberCommand(String walletId, Long version, String phoneNumber) {
        this.walletId = walletId;
        this.version = version;
        this.phoneNumber = phoneNumber;
    }
}