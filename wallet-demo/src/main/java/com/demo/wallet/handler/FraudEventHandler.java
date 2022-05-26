package com.demo.wallet.handler;

import com.demo.wallet.event.DepositedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.SourceId;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("fraud")//It is a Subscribing Event Processors.
public class FraudEventHandler {

    @EventHandler//act on incoming event
    public void on(DepositedEvent event, @SourceId String walletId) {
        //the place where ou would put your business logic to be performed when an event is received

        //feed fraudApi for deposit event
        log.info("{} for wallet: {} is sent to fraud ", event.toString(), walletId);
    }
}
