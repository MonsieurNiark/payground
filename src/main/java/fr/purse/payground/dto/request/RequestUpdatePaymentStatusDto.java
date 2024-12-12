package fr.purse.payground.dto.request;

import fr.purse.payground.dto.PaymentStatusEnum;
import lombok.Data;

/**
 * Request Payload to update a payment status
 */
@Data
public class RequestUpdatePaymentStatusDto {
    private int paymentId;
    private PaymentStatusEnum status;
}
