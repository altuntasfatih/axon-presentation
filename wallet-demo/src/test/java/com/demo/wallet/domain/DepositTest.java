package com.demo.wallet.domain;

import com.demo.wallet.command.DepositCommand;
import com.demo.wallet.event.DepositedEvent;
import com.demo.wallet.event.WalletCreatedEvent;
import com.demo.wallet.exception.DepositLimitExceedException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.TestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

public class DepositTest {

    private static final String WALLET_ID = "TEST";
    private TestExecutor<Wallet> textFixture;

    @BeforeEach
    public void setUp() {
        textFixture = new AggregateTestFixture<>(Wallet.class)
                .given(new WalletCreatedEvent(WALLET_ID, BigDecimal.ZERO));
    }

    @Test
    public void it_should_publish_depositedEvent() {
        final BigDecimal depositAmount = new BigDecimal("100.00");
        textFixture.when(new DepositCommand(WALLET_ID, depositAmount))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new DepositedEvent(depositAmount))
                .expectState(wallet -> {
                    assertThat(wallet.getBalance(), comparesEqualTo(depositAmount));
                });
    }

    @Test
    public void it_should_throw_depositLimitException() {
        final BigDecimal depositAmount = new BigDecimal("300.00");
        textFixture.andGiven(new DepositedEvent(new BigDecimal("500.00")))
                .when(new DepositCommand(WALLET_ID, depositAmount))
                .expectException(DepositLimitExceedException.class);
    }

}
