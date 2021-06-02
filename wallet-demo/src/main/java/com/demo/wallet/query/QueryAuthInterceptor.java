package com.demo.wallet.query;

import com.demo.wallet.exception.InvalidAccessException;
import com.demo.wallet.query.model.GetWalletByIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueryAuthInterceptor implements MessageHandlerInterceptor<QueryMessage<?, ?>> {
    //Message Handler Interceptors can take action both before and after query processing.
    @Override
    public Object handle(UnitOfWork<? extends QueryMessage<?, ?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        final QueryMessage<?, ?> message = unitOfWork.getMessage();
        log.info("QueryAuthInterceptor was invoked for {}", message.toString());

        final Object payload = message.getPayload();

        if (payload instanceof GetWalletByIdQuery) {
            if (((GetWalletByIdQuery) payload).getWalletId().compareToIgnoreCase("Invalid") == 0) {
                throw new InvalidAccessException();
            }
        }
        return interceptorChain.proceed();

    }
}
