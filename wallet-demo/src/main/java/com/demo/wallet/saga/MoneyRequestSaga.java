package com.demo.wallet.saga;

import com.demo.wallet.command.MoneyTransferCommand;
import com.demo.wallet.command.RollBackMoneyTransferCommand;
import com.demo.wallet.event.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.annotation.SourceId;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Saga
public class MoneyRequestSaga {

    private String from;
    private String to;
    private BigDecimal amount;
    private Boolean approved;

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestedEvent event, @SourceId String from) {
        this.from = from;
        this.to = event.getTo();
        amount = event.getAmount();
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestApprovedEvent event, @SourceId String id) {
        if (id.compareToIgnoreCase(to) == 0) {
            this.approved = true;
            commandGateway.sendAndWait(new MoneyTransferCommand());
        }
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestRejectedEvent event, @SourceId String id) {
        if (id.compareToIgnoreCase(to) == 0) {
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyTransferedEvent event, @SourceId String id) {
        if (from.compareToIgnoreCase(to) == 0) {
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyTransferFailedEvent event, @SourceId String id) {
        if (approved) {
            commandGateway.sendAndWait(new RollBackMoneyTransferCommand());
        }
        SagaLifecycle.end();
    }
}
