package fr.purse.payground.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO of {@link fr.purse.payground.entity.Order}
 */
@Data
public class OrderDto {

    private Integer id;
    private String productName;
    private String productReference;
    private int quantity;
    private BigDecimal price;
    private Integer paymentId;
}
