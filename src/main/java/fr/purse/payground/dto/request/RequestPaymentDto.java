package fr.purse.payground.dto.request;

import fr.purse.payground.dto.PaymentMethodEnum;
import lombok.Data;

import java.util.List;

/**
 * Request Payload to create a payment
 */
@Data
public class RequestPaymentDto {

    private String currency;
    private PaymentMethodEnum paymentMethod;
    private List<Integer> ordersId;

}
