package fr.purse.payground.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * Order entity
 */
@Table("orders")
@Data
public class Order {

    @Id
    @Column("id")
    private Integer id;

    @Column("product_name")
    private String productName;

    @Column("product_reference")
    private String productReference;

    @Column("quantity")
    private int quantity;

    @Column("price")
    private BigDecimal price;

    @Column("payment_id")
    private Integer paymentId;


}
