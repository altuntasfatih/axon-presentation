package com.demo.wallet.controller;

import com.demo.wallet.command.ApproveMoneyRequestCommand;
import com.demo.wallet.command.RequestMoneyCommand;
import com.demo.wallet.command.RejectMoneyRequestCommand;
import com.demo.wallet.message.ApproveMoneyRequest;
import com.demo.wallet.message.MoneyRequest;
import com.demo.wallet.message.RejectMoneyRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/wallets/")
@RestController
public class MoneyRequestController {
    private final CommandGateway commandGateway;

    public MoneyRequestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/{walletId}/request-money")
    public void requestMoney(@PathVariable("walletId") String walletId, @RequestBody MoneyRequest request) {
        var moneyRequestCommand = new RequestMoneyCommand(walletId, request.getRequestId(), request.getFromId(), request.getAmount());
        commandGateway.sendAndWait(moneyRequestCommand);
    }

    @PostMapping("/{walletId}/approve-request")
    public void approveMoneyRequest(@PathVariable("walletId") String walletId, @RequestBody ApproveMoneyRequest request) {
        var approveMoneyRequestCommand = new ApproveMoneyRequestCommand(walletId, request.getRequestId(), request.getFromId(), request.getAmount());
        commandGateway.sendAndWait(approveMoneyRequestCommand);
    }

    @PostMapping("/{walletId}/reject-request")
    public void rejectMoneyRequest(@PathVariable("walletId") String walletId, @RequestBody RejectMoneyRequest request) {
        var rejectMoneyRequestCommand = new RejectMoneyRequestCommand(walletId, request.getRequestId(), request.getFromId(), request.getAmount());
        commandGateway.sendAndWait(rejectMoneyRequestCommand);
    }
}
