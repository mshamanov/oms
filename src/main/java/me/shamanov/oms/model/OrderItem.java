package me.shamanov.oms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @NotNull(message = "Order must not be null")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Product amount must not be null")
    @PositiveOrZero(message = "Invalid product amount")
    @Column
    private Long amount;

    @NotNull(message = "Product price must not be null")
    @Digits(integer = 19, fraction = 2)
    @Positive
    @Column
    private BigDecimal price;

    @NotNull(message = "Total product price must not be null")
    @Digits(integer = 19, fraction = 2)
    @Positive
    @Column
    private BigDecimal totalPrice;
}
