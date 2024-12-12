package fr.purse.payground.control;

import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.request.RequestOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface for Order
 */
public interface OrderControlService {

    /**
     * Retrieve an order by its ID
     *
     * @param id the order id
     * @return a Mono of OrderDto
     */
    Mono<OrderDto> findOrderById(int id);

    /**
     * Create an order
     *
     * @param requestOrderDto a request order
     * @return a Mono of OrderDto
     */
    Mono<OrderDto> createOrder(RequestOrderDto requestOrderDto);

    /**
     * Find a list of orders based on the payment ID
     *
     * @param paymentId the payment ID
     * @return a flux of OrderDto
     */
    Flux<OrderDto> findOrdersByPaymentId(int paymentId);

    /**
     * Update the payment ID of an Order
     *
     * @param orderId   the order ID
     * @param paymentId the payment ID
     * @return a Mono of OrderDto
     */
    Mono<OrderDto> updatePaymentId(int orderId, int paymentId);

}
