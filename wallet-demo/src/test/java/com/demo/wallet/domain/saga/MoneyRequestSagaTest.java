package com.demo.wallet.domain.saga;

import com.demo.wallet.event.MoneyRequestedEvent;
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
    public void it_should_publish_walletCreatedEvent() {

        fixture.givenAggregate(requesterId).published()
                .whenAggregate(requesterId).publishes(new MoneyRequestedEvent(requestId, amount, fromId))
                .expectActiveSagas(1);
    }
}
