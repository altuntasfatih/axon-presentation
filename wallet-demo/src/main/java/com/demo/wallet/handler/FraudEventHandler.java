package com.demo.wallet.handler;

import com.demo.wallet.event.DepositedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.SourceId;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FraudEventHandler {

    @EventHandler//act on incoming event
    public void handle(DepositedEvent event, @SourceId String walletId) {
        //feed fraudApi for deposit event
        log.info("{} is sent to fraud ", event.toString());
    }
}
