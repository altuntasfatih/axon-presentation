package com.demo.wallet.saga;

import com.demo.wallet.command.CompleteMoneyRequestCommand;
import com.demo.wallet.command.RollBackMoneyTransferCommand;
import com.demo.wallet.event.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.annotation.SourceId;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoneyRequestSaga {

    private String requesterId;
    private String fromId;
    private String requestId;
    private Boolean approved;

    @Autowired
    @JsonIgnore
    private CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestedEvent event, @SourceId String requesterId) {
        this.requesterId = requesterId;
        this.fromId = event.getFromId();
        this.requestId = event.getRequestId();
        this.approved = false;
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestApprovedEvent event, @SourceId String id) {
        if (fromId.compareToIgnoreCase(id) == 0) {
            this.approved = true;
            commandGateway.sendAndWait(new CompleteMoneyRequestCommand(requesterId, requestId));
        }
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestRejectedEvent event, @SourceId String id) {
        if (fromId.compareToIgnoreCase(id) == 0) {
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestCompletedEvent event, @SourceId String id) {
        if (requesterId.compareToIgnoreCase(id) == 0) {
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "requestId")
    public void on(MoneyRequestFailedEvent event, @SourceId String id) {
        if (approved) {
            commandGateway.sendAndWait(new RollBackMoneyTransferCommand());
            SagaLifecycle.end();
        }
    }
}
