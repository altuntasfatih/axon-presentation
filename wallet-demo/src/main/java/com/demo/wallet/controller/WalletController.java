package com.demo.wallet.controller;

import com.demo.wallet.message.ChangePhoneNumberRequest;
import com.demo.wallet.message.DepositRequest;
import com.demo.wallet.message.WithdrawRequest;
import com.demo.wallet.service.WalletService;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/v1/wallets")
@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/{walletId}")
    public void create(@PathVariable("walletId") String walletId) {
        walletService.create(walletId);
    }

    @PostMapping("/{walletId}/deposit")
    public void deposit(@PathVariable("walletId") String walletId, @RequestBody DepositRequest request) {
        walletService.deposit(walletId, request.getAmount());
    }

    @PostMapping("/{walletId}/pay")
    public void pay(@PathVariable("walletId") String walletId, @RequestBody DepositRequest request) {
        walletService.pay(walletId, request.getAmount());
    }

    @PostMapping("/{walletId}/withdraw")
    public void withdraw(@PathVariable("walletId") String walletId, @RequestBody WithdrawRequest request) {
        walletService.withdraw(walletId, request.getVersion(), request.getAmount());
    }

    @PostMapping("/{walletId}/phoneNumber")
    public void changePhoneNumber(@PathVariable("walletId") String walletId, @RequestBody ChangePhoneNumberRequest request) {
        walletService.changePhoneNumber(walletId, request.getVersion(), request.getPhoneNumber());
    }
}
