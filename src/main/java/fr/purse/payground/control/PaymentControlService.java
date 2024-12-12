package fr.purse.payground.control;

import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.PaymentDto;
import fr.purse.payground.dto.PaymentStatusEnum;
import fr.purse.payground.dto.request.RequestPaymentDto;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Interface for payment
 */
public interface PaymentControlService {

    /**
     * Retrieve a payment by its id
     *
     * @param id the payment id
     * @return a Mono of PaymentDto
     */
    Mono<PaymentDto> findPaymentById(final int id);

    /**
     * Create a payment
     *
     * @param requestPaymentDto the payment request
     * @param orderDtoList      the list of orders
     * @return a Mono of PaymentDto
     */
    Mono<PaymentDto> createPayment(final RequestPaymentDto requestPaymentDto, final List<OrderDto> orderDtoList);

    /**
     * Update the payment status
     *
     * @param paymentId the payment id
     * @param status    the status
     * @return a Mono of PaymentDto
     */
    Mono<PaymentDto> updatePaymentStatus(final int paymentId, final PaymentStatusEnum status);
}
