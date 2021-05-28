package com.demo.wallet.domain;

import com.demo.wallet.command.CreateWalletCommand;
import com.demo.wallet.event.WalletCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate//a.k.a know as Aggregate root(Event Sources Aggregate)
@NoArgsConstructor
public class Wallet {

    @AggregateIdentifier
    private String walletId;// it is hard requirements

    private BigDecimal balance;

    @CommandHandler
    public Wallet(CreateWalletCommand command) {
        var event = new WalletCreatedEvent(command.getWalletId(), BigDecimal.ZERO);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void handle(WalletCreatedEvent event) {
        this.walletId = event.getWalletId();//it is mandatory for first event :)
        this.balance = event.getBalance();
    }
}
