package com.demo.wallet.domain;

import com.demo.wallet.command.*;
import com.demo.wallet.event.*;
import com.demo.wallet.exception.DepositLimitExceedException;
import com.demo.wallet.exception.InsufficientFundsException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.conflictresolution.ConflictResolver;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate(snapshotTriggerDefinition = "walletSnapshotTrigger")//a.k.a know as Aggregate root(Event Sources Aggregate)
@Getter
@NoArgsConstructor
public class Wallet {
    public static final BigDecimal DEPOSIT_LIMIT = new BigDecimal("750.00");

    @AggregateIdentifier
    private String walletId;// it is hard requirements
    private BigDecimal balance;
    private String phoneNumber = "";

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

    @CommandHandler
    public void handle(DepositCommand command) {
        //the place where you would put your decision-making/business logic.Because aggregate is in the correct state to decide
        final BigDecimal depositAmount = command.getDepositAmount();

        checkDepositLimit(command.getDepositAmount());

        var event = new DepositedEvent(depositAmount);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler//it used to form aggregate current state.(a.k.s Aggregation)
    protected void handle(DepositedEvent event) {
        //the place where you would change the aggregate state.
        this.balance = this.balance.add(event.getDepositAmount());
    }

    @CommandHandler
    public void handle(PayCommand command) {
        final BigDecimal payAmount = command.getPayAmount();

        checkBalance(payAmount);

        var event = new PaidEvent(payAmount);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void handle(PaidEvent event) {
        //the place where you would change the aggregate state.
        this.balance = this.balance.subtract(event.getPayAmount());
    }

    @CommandHandler//if we don't provide a conflictResolver, it expects to last version come
    public void handle(WithdrawCommand command) {
        final BigDecimal withdrawAmount = command.getWithdrawAmount();

        checkBalance(withdrawAmount);

        var event = new WithdrawnEvent(withdrawAmount);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void handle(WithdrawnEvent event) {
        //the place where you would change the aggregate state.
        this.balance = this.balance.subtract(event.getWithdrawAmount());
    }

    @CommandHandler
    public void handle(ChangePhoneNumberCommand command, ConflictResolver conflictResolver) {
        /*
            If PhoneNumberChangedEvent events have taken place since the targeted version, that's a conflict.
            other events like WithdrawnEvent,DepositedEvent are no problem.They never create a conflict on phoneNumber state.
         */
        conflictResolver.detectConflicts(
                events -> events.stream().anyMatch(
                        event -> event.getPayloadType().equals(PhoneNumberChangedEvent.class)));

        var event = new PhoneNumberChangedEvent(command.getPhoneNumber());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void handle(PhoneNumberChangedEvent event) {
        this.phoneNumber = event.getPhoneNumber();
    }

    private void checkDepositLimit(BigDecimal depositAmount) {
        if (this.balance.add(depositAmount).compareTo(BigDecimal.valueOf(750)) > 0) {
            throw new DepositLimitExceedException();
        }
    }

    private void checkBalance(BigDecimal requestAmount) {
        if (balance.compareTo(requestAmount) < 0) {
            throw new InsufficientFundsException();
        }
    }
}
