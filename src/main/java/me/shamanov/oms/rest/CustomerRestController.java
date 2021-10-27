package me.shamanov.oms.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import me.shamanov.oms.dto.Customers;
import me.shamanov.oms.model.Customer;
import me.shamanov.oms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = {"Customers"})
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Customers as JSON/XML", description = "Get all customers in JSON/XML format")
    @GetMapping(value = "/customers", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Customers getCustomers() {
        List<Customer> customerList = this.customerService.getAllCustomers();
        return new Customers(customerList);
    }
}
