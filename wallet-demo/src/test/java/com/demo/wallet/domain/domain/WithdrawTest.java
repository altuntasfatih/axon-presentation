package com.demo.wallet.domain.domain;

import com.demo.wallet.command.WithdrawCommand;
import com.demo.wallet.domain.Wallet;
import com.demo.wallet.event.DepositedEvent;
import com.demo.wallet.event.WalletCreatedEvent;
import com.demo.wallet.event.WithdrawnEvent;
import com.demo.wallet.exception.InsufficientFundsException;
import org.axonframework.modelling.command.ConflictingAggregateVersionException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.TestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

public class WithdrawTest {

    private static final String WALLET_ID = "TEST";
    private static final Long VERSION = 1L;

    private TestExecutor<Wallet> textFixture;

    @BeforeEach
    public void setUp() {
        textFixture = new AggregateTestFixture<>(Wallet.class)
                .given(new WalletCreatedEvent(WALLET_ID, BigDecimal.ZERO))
                .andGiven(new DepositedEvent(new BigDecimal("500.00")));
    }

    @Test
    public void it_should_publish_withdrawnEvent() {
        final BigDecimal withdrawAmount = new BigDecimal("100.00");
        textFixture.when(new WithdrawCommand(WALLET_ID, VERSION, withdrawAmount))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new WithdrawnEvent(withdrawAmount))
                .expectState(wallet -> {
                    assertThat(wallet.getBalance(), comparesEqualTo(new BigDecimal("400")));
                });

    }

    @Test
    public void it_should_throw_insufficientFundsException() {
        final BigDecimal withdrawAmount = new BigDecimal("600.00");
        textFixture.when(new WithdrawCommand(WALLET_ID, VERSION, withdrawAmount))
                .expectException(InsufficientFundsException.class);
    }

    @Test
    public void it_should_throw_conflict_error_when_expected_version_is_not_match() {
        final BigDecimal withdrawAmount = new BigDecimal("100.00");
        textFixture.when(new WithdrawCommand(WALLET_ID, 3L, withdrawAmount))
                .expectException(ConflictingAggregateVersionException.class);
    }


}
