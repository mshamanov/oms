package me.shamanov.oms.service;

import me.shamanov.oms.model.Customer;
import me.shamanov.oms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public List<Customer> getAllCustomers(Sort sort) {
        return this.customerRepository.findAll(sort);
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return this.customerRepository.findAll(pageable);
    }

    public Page<Customer> getAllCustomersOrderByOrdersSizeAsc(Pageable pageable) {
        return this.customerRepository.getAllCustomersOrderByOrdersSizeAsc(pageable);
    }

    public Page<Customer> getAllCustomersOrderByOrdersSizeDesc(Pageable pageable) {
        return this.customerRepository.getAllCustomersOrderByOrdersSizeDesc(pageable);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return this.customerRepository.findById(id);
    }

    public Page<Customer> getCustomersBySearchValue(String value, Pageable pageable) {
        Customer customer = new Customer();
        customer.setFirstName(value);
        customer.setLastName(value);
        customer.setEmail(value);
        customer.setPhoneNumber(value);

        ExampleMatcher.GenericPropertyMatcher propertyMatcher =
                ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase();
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                                                      .withMatcher("firstName", propertyMatcher)
                                                      .withMatcher("lastName", propertyMatcher)
                                                      .withMatcher("email", propertyMatcher)
                                                      .withMatcher("phoneNumber", propertyMatcher);
        Example<Customer> customerExample = Example.of(customer, exampleMatcher);
        return this.customerRepository.findAll(customerExample, pageable);
    }


    public Customer saveCustomer(Customer customer) {
        return this.customerRepository.save(customer);
    }

    public void deleteCustomer(Customer customer) {
        this.customerRepository.delete(customer);

    }

    public void deleteCustomerById(Long id) {
        this.customerRepository.deleteById(id);
    }

    public boolean isCustomerExists(Long id) {
        return this.customerRepository.existsById(id);
    }

    public long getTotalCount() {
        return this.customerRepository.count();
    }

    public List<Map<String, Long>> getCustomerWithMaxOrders(Pageable pageable) {
        return this.customerRepository.getCustomerWithMaxOrders(pageable);
    }
}
