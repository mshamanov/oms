package me.shamanov.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @NotNull(message = "Order must not be null")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull(message = "Product id must not be null")
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Product amount must not be null")
    @PositiveOrZero(message = "Invalid product amount")
    @Column
    private Long amount;

    @NotNull(message = "Product price must not be null")
    @Positive(message = "Product price must be a positive number")
    @Column
    private BigDecimal price;

    @NotNull(message = "Total product price must not be null")
    @Positive(message = "Total product price must be a positive number")
    @Column
    private BigDecimal totalPrice;
}
