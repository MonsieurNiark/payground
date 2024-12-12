package fr.purse.payground.dto.request;

import fr.purse.payground.dto.PaymentDto;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Request Payload to create an order
 */
@Getter
public class RequestOrderDto {

    private String productName;
    private String productReference;
    private int quantity;
    private BigDecimal price;
    private PaymentDto payment;
}
