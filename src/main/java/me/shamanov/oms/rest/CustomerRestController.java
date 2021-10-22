package me.shamanov.oms.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import me.shamanov.oms.dto.Customers;
import me.shamanov.oms.model.Customer;
import me.shamanov.oms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Api(tags = {"Customer Controller"})
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Customers as JSON", description = "Get all customers in JSON format")
    @GetMapping(value = {"/", "/json"}, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public Customers getCustomersAsJSON() {
        return this.getCustomers();
    }

    @Operation(summary = "Customers as XML", description = "Get all customers in XML format")
    @GetMapping(value = "/xml", produces = {MimeTypeUtils.APPLICATION_XML_VALUE})
    public Customers getCustomersAsXML() {
        return this.getCustomers();
    }

    private Customers getCustomers() {
        List<Customer> customerList = this.customerService.getAllCustomers();
        return new Customers(customerList);
    }
}
