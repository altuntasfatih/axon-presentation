package com.demo.wallet.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.serialization.Revision;

import java.math.BigDecimal;

@Revision("1.0")
@Getter
@AllArgsConstructor
public class WalletCreatedEvent {
    private String walletId;
    private BigDecimal balance;
}
