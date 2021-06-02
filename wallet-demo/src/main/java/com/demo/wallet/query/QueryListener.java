package com.demo.wallet.query;

import com.demo.wallet.query.model.GetWalletByIdQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class QueryListener {

    final WalletQueryRepository repository;

    public QueryListener(WalletQueryRepository repository) {
        this.repository = repository;
    }

    @QueryHandler// act on incoming query messages.
    public WalletModel findByAccountId(GetWalletByIdQuery query) {
        return repository.findByWalletId(query.getWalletId());
    }
}
