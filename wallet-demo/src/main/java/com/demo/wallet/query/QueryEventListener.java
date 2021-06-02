package com.demo.wallet.query;

import com.demo.wallet.event.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.SequenceNumber;
import org.axonframework.messaging.annotation.SourceId;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("query")//it is a Tracking Event Processors.Look token_entry table.It works like as outbox pattern.
public class QueryEventListener {

    private WalletQueryRepository repository;

    public QueryEventListener(WalletQueryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(WalletCreatedEvent event) {
        final WalletModel wallet = new WalletModel(event.getWalletId(), event.getBalance());
        repository.save(wallet);

        log.info("WalletQueryModel is created for {}", wallet.getWalletId());
    }

    @EventHandler
    public void on(DepositedEvent event, @SourceId String walletId, @SequenceNumber Long version) {
        final WalletModel wallet = repository.findByWalletId(walletId);
        wallet.increaseBalance(event.getDepositAmount());
        wallet.setVersion(version);
        repository.save(wallet);

        log.info("WalletQueryModel deposit event is handled for {}", wallet.getWalletId());
    }

    @EventHandler
    public void on(PaidEvent event, @SourceId String walletId, @SequenceNumber Long version) {
        final WalletModel wallet = repository.findByWalletId(walletId);
        wallet.decreaseBalance(event.getPayAmount());
        wallet.setVersion(version);
        repository.save(wallet);

        log.info("WalletQueryModel paid event is handled for {}", wallet.getWalletId());
    }

    @EventHandler
    public void on(WithdrawnEvent event, @SourceId String walletId, @SequenceNumber Long version) {
        final WalletModel wallet = repository.findByWalletId(walletId);
        wallet.decreaseBalance(event.getWithdrawAmount());
        wallet.setVersion(version);
        repository.save(wallet);

        log.info("WalletQueryModel withdrawn event is handled for {}", wallet.getWalletId());
    }

    @EventHandler
    public void on(PhoneNumberChangedEvent event, @SourceId String walletId, @SequenceNumber Long version) {
        final WalletModel wallet = repository.findByWalletId(walletId);
        wallet.setVersion(version);
        wallet.setPhoneNumber(event.getPhoneNumber());
        repository.save(wallet);

        log.info("WalletQueryModel deposit event is handled for {}", wallet.getWalletId());
    }
}
