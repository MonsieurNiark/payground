package fr.purse.payground.dto.response;

import fr.purse.payground.dto.PaymentDto;

import java.math.BigDecimal;

/**
 * Response Payload of an Order
 *
 * @param id
 * @param productName
 * @param productReference
 * @param quantity
 * @param price
 * @param payment
 */
public record ResponseOrderDto(int id, String productName, String productReference, int quantity, BigDecimal price,
                               PaymentDto payment) {
}
