package com.demo.wallet.domain.query;

import com.demo.wallet.exception.InvalidAccessException;
import com.demo.wallet.query.QueryAuthInterceptor;
import com.demo.wallet.query.model.GetWalletByIdQuery;
import org.axonframework.messaging.responsetypes.InstanceResponseType;
import org.axonframework.queryhandling.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class QueryTest {

    private static final String SUCCESS_RESULT = "ok";
    private QueryBus queryBus;

    @BeforeEach
    public void setUp() {
        queryBus = SimpleQueryBus.builder().build();
        queryBus.registerHandlerInterceptor(new QueryAuthInterceptor());
        queryBus.subscribe(GetWalletByIdQuery.class.getName(), String.class, q -> SUCCESS_RESULT);
    }

    @Test
    public void it_should_invoke_query_handler() {
        final GetWalletByIdQuery query = new GetWalletByIdQuery("1");
        final QueryResponseMessage<String> responseMessage = queryBus.query(new GenericQueryMessage<>(query, new InstanceResponseType<>(String.class))).join();

        assertThat(responseMessage.getPayload(), comparesEqualTo(SUCCESS_RESULT));
    }

    @Test
    public void it_should_throw_error_from_query_interceptor() {
        final GetWalletByIdQuery query = new GetWalletByIdQuery("Invalid");

        final GenericQueryResponseMessage<String> responseMessage = (GenericQueryResponseMessage<String>) queryBus.query(new GenericQueryMessage<>(query, new InstanceResponseType<>(String.class))).join();

        assertThat(responseMessage.isExceptional(), equalTo(Boolean.TRUE));
        assertThat(responseMessage.exceptionResult(), instanceOf(InvalidAccessException.class));
    }
}
