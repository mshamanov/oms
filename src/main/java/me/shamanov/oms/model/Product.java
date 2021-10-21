package me.shamanov.oms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product name must not be null")
    @NotEmpty(message = "Product name must not be empty")
    @Column
    private String name;

    @NotNull(message = "Product price must not be null")
    @Digits(integer = 19, fraction = 2)
    @Positive
    @Column
    private BigDecimal price;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @PreRemove
    public void preRemove() {
        this.orderItems.forEach(orderItem -> orderItem.setProduct(null));
    }
}
