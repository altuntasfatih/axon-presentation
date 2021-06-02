package com.demo.wallet.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
public class WalletModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "wallet_id")
    private String walletId;

    @Column(name = "version")
    private Long version;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "phone_number")
    private String phoneNumber = "";

    public WalletModel(String walletId, BigDecimal balance) {
        this.walletId = walletId;
        this.balance = balance;
        this.version = 0L;
    }

    public void increaseBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
