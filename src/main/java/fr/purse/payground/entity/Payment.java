package fr.purse.payground.entity;


import fr.purse.payground.dto.PaymentMethodEnum;
import fr.purse.payground.dto.PaymentStatusEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


/**
 * Payment entity
 */
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @Column("id")
    private Integer id;

    @Column("currency")
    private String currency;

    @Column("status")
    private PaymentStatusEnum status;

    @Column("payment_method")
    private PaymentMethodEnum paymentMethod;

    @Column("amount")
    private BigDecimal amount;

}
