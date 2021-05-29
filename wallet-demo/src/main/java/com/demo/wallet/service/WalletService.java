package com.demo.wallet.service;

import com.demo.wallet.command.*;
import com.demo.wallet.domain.Wallet;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final Snapshotter snapshotter;
    private final CommandGateway commandGateway;//It uses the CommandBus underneath to perform  dispatching of the message.

    public WalletService(Snapshotter snapshotter, CommandGateway commandGateway) {
        this.snapshotter = snapshotter;
        this.commandGateway = commandGateway;
    }

    public void create(String walletId) {
        final CreateWalletCommand command = new CreateWalletCommand(walletId);
        sendCommand(command);
    }

    public void deposit(String walletId, BigDecimal depositAmount) {
        final DepositCommand command = new DepositCommand(walletId, depositAmount);
        sendCommand(command);
    }

    public void pay(String walletId, BigDecimal payAmount) {
        final PayCommand command = new PayCommand(walletId, payAmount);
        sendCommand(command);
    }

    //we can trigger taking snapshot manually.
    public void takeSnapshot(String walletId) {
        snapshotter.scheduleSnapshot(Wallet.class, walletId);
    }

    protected <T> T sendCommand(Object command) {
        return commandGateway.sendAndWait(command);
    }

}
