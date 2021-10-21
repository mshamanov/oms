package me.shamanov.oms.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Valid
    @NotNull(message = "Customer must not be null")
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdOn;

    @JacksonXmlElementWrapper(localName = "OrderItems")
    @JacksonXmlProperty(localName = "OrderItem")
    @Size(min = 1, message = "Order must have at least one product")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    private List<@Valid OrderItem> orderItems;

    @PrePersist
    public void prePersist() {
        this.uid = UUID.randomUUID().toString();
        this.createdOn = new Date();
    }
}
