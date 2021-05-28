package com.demo.wallet.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.serialization.Revision;

import java.math.BigDecimal;

@Revision("1.0")//for versioning events
@Getter
@AllArgsConstructor
public class DepositedEvent {
    private BigDecimal depositAmount;
}
