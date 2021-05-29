package com.demo.wallet.domain;

import com.demo.wallet.command.PayCommand;
import com.demo.wallet.event.DepositedEvent;
import com.demo.wallet.event.PaidEvent;
import com.demo.wallet.event.WalletCreatedEvent;
import com.demo.wallet.exception.InsufficientFundsException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.TestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

public class PayTest {

    private static String WALLET_ID = "TEST";
    private TestExecutor<Wallet> textFixture;

    @BeforeEach
    public void setUp() {
        textFixture = new AggregateTestFixture<>(Wallet.class)
                .given(new WalletCreatedEvent(WALLET_ID, BigDecimal.ZERO))
                .andGiven(new DepositedEvent(new BigDecimal("500")));
    }

    @Test
    public void it_should_publish_paidEvent() {
        final BigDecimal payAmount = new BigDecimal("200.00");
        textFixture.when(new PayCommand(WALLET_ID, payAmount))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new PaidEvent(payAmount))
                .expectState(wallet -> {
                    assertThat(wallet.getBalance(), comparesEqualTo(new BigDecimal("300")));
                });
    }

    @Test
    public void it_should_throw_insufficientFundsException() {
        final BigDecimal payAmount = new BigDecimal("600.00");
        textFixture
                .when(new PayCommand(WALLET_ID, payAmount))
                .expectException(InsufficientFundsException.class);
    }
}
