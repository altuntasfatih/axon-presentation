package com.demo.wallet.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletQueryRepository extends JpaRepository<WalletModel, Long> {

    WalletModel findByWalletId(String walletId);

}
