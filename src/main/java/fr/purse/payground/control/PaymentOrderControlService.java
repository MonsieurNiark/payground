package fr.purse.payground.control;

import fr.purse.payground.dto.request.RequestPaymentDto;
import fr.purse.payground.dto.request.RequestUpdatePaymentStatusDto;
import fr.purse.payground.dto.response.ResponseOrderDto;
import fr.purse.payground.dto.response.ResponsePaymentDto;
import reactor.core.publisher.Mono;

/**
 * Interface of Payment Order
 */
public interface PaymentOrderControlService {

    /**
     * Find a payment by its id. Throw 404 if not found
     *
     * @param paymentId the payment id
     * @return a Mono of {@link ResponsePaymentDto}
     */
    Mono<ResponsePaymentDto> findPaymentById(int paymentId);

    /**
     * Doing a post create a payment binded to the given orders IDs
     * Throws 404 if one order is not found, throw a 403 is the order has already a payment
     *
     * @param requestPaymentDto the body of the request
     * @return a {@link ResponsePaymentDto}
     */
    Mono<ResponsePaymentDto> createPayment(RequestPaymentDto requestPaymentDto);

    /**
     * Update the payment status
     *
     * @param requestUpdatePaymentStatusDto the payment payload
     * @return a Mono of {@link ResponsePaymentDto}
     */
    Mono<ResponsePaymentDto> updatePaymentStatus(RequestUpdatePaymentStatusDto requestUpdatePaymentStatusDto);

    /**
     * Retrieve an order by its ID
     *
     * @param orderId the order id
     * @return a Mono of OrderDto
     */
    Mono<ResponseOrderDto> findOrderById(int orderId);
}
