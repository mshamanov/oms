package me.shamanov.oms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

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
    @Positive(message = "Product price must be a positive number")
    @Column
    private BigDecimal price;
}
