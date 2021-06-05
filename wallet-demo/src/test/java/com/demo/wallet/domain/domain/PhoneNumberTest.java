package com.demo.wallet.domain.domain;

import com.demo.wallet.command.ChangePhoneNumberCommand;
import com.demo.wallet.domain.Wallet;
import com.demo.wallet.event.DepositedEvent;
import com.demo.wallet.event.PhoneNumberChangedEvent;
import com.demo.wallet.event.WalletCreatedEvent;
import org.axonframework.modelling.command.ConflictingAggregateVersionException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.TestExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;

public class PhoneNumberTest {

    private static final String WALLET_ID = "TEST";
    private static final Long CURRENT_VERSION = 3L;

    private TestExecutor<Wallet> textFixture;

    @BeforeEach
    public void setUp() {
        textFixture = new AggregateTestFixture<>(Wallet.class)
                .given(new WalletCreatedEvent(WALLET_ID, BigDecimal.ZERO))
                .andGiven(new DepositedEvent(new BigDecimal("50.00")))
                .andGiven(new DepositedEvent(new BigDecimal("30.00")))
                .andGiven(new DepositedEvent(new BigDecimal("20.00")));
    }

    @Test
    public void it_should_publish_phoneNumberChangedEvent() {
        final String phoneNumber = "5XX123XXXX";
        textFixture.when(new ChangePhoneNumberCommand(WALLET_ID, CURRENT_VERSION - 2L, phoneNumber))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new PhoneNumberChangedEvent(phoneNumber))
                .expectState(wallet -> {
                    assertThat(wallet.getPhoneNumber(), equalTo(phoneNumber));
                });
    }

    @Test
    public void it_should_throw_conflict_error_when_there_is_also_phoneNumberChangedEvent_after_expected_version() {
        final String phoneNumber = "5XX123XXXX";
        textFixture.andGiven(new PhoneNumberChangedEvent("3XX987XXXX"))
                .when(new ChangePhoneNumberCommand(WALLET_ID, CURRENT_VERSION - 3L, phoneNumber))
                .expectException(ConflictingAggregateVersionException.class);
    }

}