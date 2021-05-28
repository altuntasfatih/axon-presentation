package com.demo.wallet.service;

import com.demo.wallet.command.CreateWalletCommand;
import com.demo.wallet.command.DepositCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final CommandGateway commandGateway;//It uses the CommandBus underneath to perform  dispatching of the message.

    public WalletService(CommandGateway commandGateway) {
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

    protected <T> T sendCommand(Object command) {
        return commandGateway.sendAndWait(command);
    }
}
