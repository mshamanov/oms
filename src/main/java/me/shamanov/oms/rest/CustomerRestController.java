package me.shamanov.oms.rest;

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
@RequestMapping("/api")
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = {"/", "/customers/json"}, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public Customers getCustomersAsJSON() {
        return this.getCustomers();
    }

    @GetMapping(value = "/customers/xml", produces = {MimeTypeUtils.APPLICATION_XML_VALUE})
    public Customers getCustomersAsXML() {
        return this.getCustomers();
    }

    private Customers getCustomers() {
        List<Customer> customerList = this.customerService.getAllCustomers();
        return new Customers(customerList);
    }
}
