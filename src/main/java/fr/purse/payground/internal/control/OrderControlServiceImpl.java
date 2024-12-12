package fr.purse.payground.internal.control;

import fr.purse.payground.control.OrderControlService;
import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.request.RequestOrderDto;
import fr.purse.payground.mapper.OrderMapper;
import fr.purse.payground.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link OrderControlService}
 */
@Service
public class OrderControlServiceImpl implements OrderControlService {
    private static final OrderMapper ORDER_MAPPER = OrderMapper.INSTANCE;

    private final OrderRepository orderRepository;

    /**
     * Constructor
     *
     * @param orderRepositoryParam repository of Order
     */
    @Autowired
    public OrderControlServiceImpl(OrderRepository orderRepositoryParam) {
        this.orderRepository = orderRepositoryParam;
    }

    @Override
    public Mono<OrderDto> findOrderById(final int id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")))
                .map(ORDER_MAPPER::toDto);
    }

    @Override
    public Mono<OrderDto> createOrder(final RequestOrderDto orderDto) {
        return orderRepository.save(ORDER_MAPPER.fromDto(orderDto)).map(ORDER_MAPPER::toDto);
    }

    @Override
    public Flux<OrderDto> findOrdersByPaymentId(final int id) {
        return orderRepository.findByPaymentId(id).map(ORDER_MAPPER::toDto);
    }

    @Override
    public Mono<OrderDto> updatePaymentId(final int orderId, final int paymentId) {
        return findOrderById(orderId).flatMap(orderDto -> {
            orderDto.setPaymentId(paymentId);
            return orderRepository.save(ORDER_MAPPER.fromDto(orderDto)).map(ORDER_MAPPER::toDto);
        });
    }

}
