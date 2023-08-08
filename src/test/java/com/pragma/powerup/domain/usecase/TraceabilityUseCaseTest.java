package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.common.exception.ClientAuthMustBeEqualsClientOrderException;
import com.pragma.powerup.common.exception.NoDataFoundException;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.bearertoken.IToken;
import com.pragma.powerup.domain.spi.feignclients.ITraceabilityFeignClientPort;
import com.pragma.powerup.factory.FactoryOrdersDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TraceabilityUseCaseTest {

    @InjectMocks
    TraceabilityUseCase traceabilityUseCase;

    @Mock
    ITraceabilityFeignClientPort traceabilityFeignClientPort;

    @Mock
    IToken token;

    @Mock
    IOrderPersistencePort orderPersistencePort;

    @Test
    void getAllTraceability() {
        Long orderId = 1L;
        validateToken(1L);
        List<Traceability> traceabilityList = new ArrayList<>();
        traceabilityList.add(new Traceability());

        Order order = FactoryOrdersDataTest.getOrder();
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        Mockito.when(traceabilityFeignClientPort.getAllTraceability(orderId)).thenReturn(traceabilityList);

        traceabilityUseCase.getAllTraceability(orderId);

        Mockito.verify(token).getBearerToken();
        Mockito.verify(token).getUserAuthenticatedId("bearer token");
        Mockito.verify(orderPersistencePort, Mockito.times(1)).getOrderById(orderId);
    }

    @Test
    void getAllTraceabilityWithClientAuthMustBeEqualsClientOrderException() {
        Long orderId = 1L;
        validateToken(2L);
        List<Traceability> traceabilityList = new ArrayList<>();
        traceabilityList.add(new Traceability());

        Order order = FactoryOrdersDataTest.getOrder();
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        Mockito.when(traceabilityFeignClientPort.getAllTraceability(orderId)).thenReturn(traceabilityList);

        assertThrows(ClientAuthMustBeEqualsClientOrderException.class, () -> traceabilityUseCase.getAllTraceability(orderId));

        Mockito.verify(token).getBearerToken();
        Mockito.verify(token).getUserAuthenticatedId("bearer token");
        Mockito.verify(orderPersistencePort, Mockito.times(1)).getOrderById(orderId);
    }

    @Test
    void getAllTraceabilityWithNoDataFoundException() {
        Long orderId = 1L;
        validateToken(1L);
        List<Traceability> traceabilityList = new ArrayList<>();

        Order order = FactoryOrdersDataTest.getOrder();
        Mockito.when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        Mockito.when(traceabilityFeignClientPort.getAllTraceability(orderId)).thenReturn(traceabilityList);

        assertThrows(NoDataFoundException.class, () -> traceabilityUseCase.getAllTraceability(orderId));

        Mockito.verify(token).getBearerToken();
        Mockito.verify(token).getUserAuthenticatedId("bearer token");
        Mockito.verify(orderPersistencePort, Mockito.times(1)).getOrderById(orderId);
    }

    private void validateToken(Long id) {
        Mockito.when(token.getBearerToken()).thenReturn("bearer token");
        Mockito.when(token.getUserAuthenticatedId("bearer token")).thenReturn(id);
    }
}
