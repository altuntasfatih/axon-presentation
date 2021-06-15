package com.demo.wallet.controller;

import com.demo.wallet.message.DepositRequest;
import com.demo.wallet.message.PayRequest;
import com.demo.wallet.message.RefundRequest;
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
    public void pay(@PathVariable("walletId") String walletId, @RequestBody PayRequest request) {
        walletService.pay(walletId, request.getOrderId(), request.getAmount());
    }

    @PostMapping("/{walletId}/refund")
    public void refund(@PathVariable("walletId") String walletId, @RequestBody RefundRequest request) {
        walletService.refund(walletId, request.getOrderId(), request.getRefundAmount());
    }
}
