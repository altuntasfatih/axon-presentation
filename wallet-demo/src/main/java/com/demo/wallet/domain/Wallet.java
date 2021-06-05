package com.demo.wallet.domain;

import com.demo.wallet.command.CreateWalletCommand;
import com.demo.wallet.command.DepositCommand;
import com.demo.wallet.command.PayCommand;
import com.demo.wallet.event.DepositedEvent;
import com.demo.wallet.event.PaidEvent;
import com.demo.wallet.event.WalletCreatedEvent;
import com.demo.wallet.exception.DepositLimitExceedException;
import com.demo.wallet.exception.InsufficientFundsException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
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

    @CommandHandler
    public Wallet(CreateWalletCommand command) {
        var event = new WalletCreatedEvent(command.getWalletId(), BigDecimal.ZERO);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    protected void on(WalletCreatedEvent event) {
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
    protected void on(DepositedEvent event) {
        //the place where you would change the aggregate state.
        this.balance = this.balance.add(event.getDepositAmount());
    }

    @CommandHandler
    public void handle(PayCommand command) {
        //the place where you would put your decision-making/business logic.Because aggregate is in the correct state to decide
        final BigDecimal payAmount = command.getPayAmount();

        checkBalance(payAmount);

        var event = new PaidEvent(payAmount);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler//it used to form aggregate current state.(a.k.s Aggregation)
    protected void on(PaidEvent event) {
        //the place where you would change the aggregate state.
        this.balance = this.balance.subtract(event.getPayAmount());
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
