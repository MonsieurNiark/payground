package fr.purse.payground.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO of {@link fr.purse.payground.entity.Payment}
 */
@Data
public class PaymentDto {
    private Integer id;
    private String currency;
    private PaymentStatusEnum status;
    private PaymentMethodEnum paymentMethod;
    private BigDecimal amount;
}
