package me.shamanov.oms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer firstName must not be null")
    @NotEmpty(message = "Customer firstName must not be empty")
    @Column(nullable = false)
    private String firstName;

    @NotNull(message = "Customer lastName must not be null")
    @NotEmpty(message = "Customer lastName must not be empty")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Customer email must not be null")
    @NotEmpty(message = "Customer email must not be empty")
    @Email
    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders;
}
