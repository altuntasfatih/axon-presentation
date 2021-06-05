package com.demo.wallet.controller;

import com.demo.wallet.query.WalletModel;
import com.demo.wallet.query.model.GetWalletByIdQuery;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequestMapping("/v1/query/wallets")
@RestController
public class QueryController {
    private final QueryGateway queryGateway;//It uses the QueryBus underneath to perform dispatching of the query to query handlers..

    public QueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/{walletId}")
    public CompletableFuture<WalletModel> getWallet(@PathVariable("walletId") String walletId) {
        final GetWalletByIdQuery query = new GetWalletByIdQuery(walletId);
        return queryGateway.query(query, WalletModel.class);
    }
}
