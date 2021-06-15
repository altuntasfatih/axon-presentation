package com.demo.wallet.service;

import com.demo.wallet.command.*;
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

    public void pay(String walletId, String orderId, BigDecimal payAmount) {
        final PayCommand command = new PayCommand(walletId, orderId, payAmount);
        sendCommand(command);
    }

    public void refund(String walletId, String orderId, BigDecimal refundAmount) {
        final RefundCommand command = new RefundCommand(walletId, orderId, refundAmount);
        sendCommand(command);
    }

    protected <T> T sendCommand(Object command) {
        return commandGateway.sendAndWait(command);
    }


}
