package com.demo.wallet.domain.saga;

import com.demo.wallet.command.CompleteMoneyRequestCommand;
import com.demo.wallet.command.RollBackMoneyTransferCommand;
import com.demo.wallet.event.*;
import com.demo.wallet.saga.MoneyRequestSaga;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class MoneyRequestSagaTest {
    private static final String requesterId = "wallet-A";
    private static final String requestId = "request-1";
    private static final String fromId = "wallet-B";
    private static final BigDecimal amount = new BigDecimal("20.0");

    private SagaTestFixture fixture;

    @BeforeEach
    public void setUp() {
        fixture = new SagaTestFixture<>(MoneyRequestSaga.class);
    }

    @Test
    public void it_should_start_saga() {
        fixture.givenAggregate(requesterId).published()
                .whenAggregate(requesterId).publishes(new MoneyRequestedEvent(requestId, amount, fromId))
                .expectActiveSagas(1)
                .expectNoDispatchedCommands();
    }

    @Test
    public void it_should_send_complete_money_command() {
        fixture.givenAggregate(requesterId)
                .published(new MoneyRequestedEvent(requestId, amount, fromId))
                .whenAggregate(fromId).publishes(new MoneyRequestApprovedEvent(requestId, amount))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new CompleteMoneyRequestCommand(requesterId, requestId));
    }

    @Test
    public void it_should_end_saga_when_MoneyRequestRejectedEvent_published() {
        fixture.givenAggregate(requesterId)
                .published(new MoneyRequestedEvent(requestId, amount, fromId))
                .whenAggregate(fromId).publishes(new MoneyRequestRejectedEvent(requestId))
                .expectNoDispatchedCommands()
                .expectActiveSagas(0);
    }

    @Test
    public void it_should_end_saga_when_MoneyRequestCompletedEvent_published() {
        fixture.givenAggregate(requesterId)
                .published(new MoneyRequestedEvent(requestId, amount, fromId))
                .andThenAggregate(fromId).published(new MoneyRequestApprovedEvent(requestId, amount))
                .whenAggregate(requesterId).publishes(new MoneyRequestCompletedEvent(requestId, amount, requesterId))
                .expectNoDispatchedCommands()
                .expectActiveSagas(0);
    }

    @Test
    public void it_should_end_saga_and_send_rollback_command_when_MoneyRequestFailedEvent_published() {
        fixture.givenAggregate(requesterId)
                .published(new MoneyRequestedEvent(requestId, amount, fromId))
                .andThenAggregate(fromId).published(new MoneyRequestApprovedEvent(requestId, amount))
                .whenAggregate(requesterId).publishes(new MoneyRequestFailedEvent(requestId, amount, requesterId))
                .expectDispatchedCommands(new RollBackMoneyTransferCommand(fromId, requestId, amount))
                .expectActiveSagas(0);
    }
}
