package com.demo.wallet.domain;

import com.demo.wallet.command.CreateWalletCommand;
import com.demo.wallet.event.WalletCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class WalletTest {
    private static final String WALLET_ID = "TEST";
    private FixtureConfiguration<Wallet> textFixture;

    @BeforeEach
    public void setUp() {
        textFixture = new AggregateTestFixture<>(Wallet.class);
    }

    @Test
    public void it_should_publish_walletCreatedEvent() {

        textFixture.given()
                .when(new CreateWalletCommand(WALLET_ID))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new WalletCreatedEvent(WALLET_ID, BigDecimal.ZERO));
    }
}
