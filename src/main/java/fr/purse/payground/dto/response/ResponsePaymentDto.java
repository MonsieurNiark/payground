package fr.purse.payground.dto.response;

import fr.purse.payground.dto.OrderDto;
import fr.purse.payground.dto.PaymentMethodEnum;
import fr.purse.payground.dto.PaymentStatusEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response payload of a payment
 *
 * @param id
 * @param currency
 * @param status
 * @param paymentMethod
 * @param amount
 * @param listOrders
 */
public record ResponsePaymentDto(Integer id, String currency, PaymentStatusEnum status, PaymentMethodEnum paymentMethod,
                                 BigDecimal amount, List<OrderDto> listOrders) {

}
