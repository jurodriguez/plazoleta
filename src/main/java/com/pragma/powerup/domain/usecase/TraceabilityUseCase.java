package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.ClientAuthMustBeEqualsClientOrderException;
import com.pragma.powerup.common.exception.NoDataFoundException;
import com.pragma.powerup.common.exception.OrderNotExistException;
import com.pragma.powerup.common.exception.OwnerNotAuthenticatedException;
import com.pragma.powerup.domain.api.ITraceabilityServicePort;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.domain.spi.feignclients.ITraceabilityFeignClientPort;

import java.util.List;

public class TraceabilityUseCase implements ITraceabilityServicePort {

    private final ITraceabilityFeignClientPort traceabilityFeignClientPort;

    private final IToken token;

    private final IOrderPersistencePort orderPersistencePort;

    public TraceabilityUseCase(ITraceabilityFeignClientPort traceabilityFeignClientPort, IToken token, IOrderPersistencePort orderPersistencePort) {
        this.traceabilityFeignClientPort = traceabilityFeignClientPort;
        this.token = token;
        this.orderPersistencePort = orderPersistencePort;
    }

    @Override
    public List<Traceability> getAllTraceability(Long orderId) {
        Long ownerAuthId = getOwnerAuth();
        Order order = getOrderById(orderId);

        if (ownerAuthId.longValue() != order.getCustomerId().longValue())
            throw new ClientAuthMustBeEqualsClientOrderException();

        List<Traceability> traceabilityList = traceabilityFeignClientPort.getAllTraceability(orderId);
        if (traceabilityList.isEmpty()) throw new NoDataFoundException();
        return traceabilityList;
    }

    private Long getOwnerAuth() {
        String bearerToken = token.getBearerToken();
        if (bearerToken == null) throw new OwnerNotAuthenticatedException();
        return token.getUserAuthenticatedId(bearerToken);
    }

    private Order getOrderById(Long orderId) {
        Order order = orderPersistencePort.getOrderById(orderId);
        if (order == null) throw new OrderNotExistException();
        return order;
    }
}
