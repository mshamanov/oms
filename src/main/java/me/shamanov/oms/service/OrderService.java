package me.shamanov.oms.service;

import me.shamanov.oms.model.Customer;
import me.shamanov.oms.model.Order;
import me.shamanov.oms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders(Sort sort) {
        return this.orderRepository.findAll(sort);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersBySearchValue(String value, Pageable pageable) {
        Order order = new Order();
        order.setUid(value);
        Customer customer = new Customer();
        customer.setFirstName(value);
        customer.setLastName(value);
        order.setCustomer(customer);

        ExampleMatcher.GenericPropertyMatcher propertyMatcher =
                ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase();
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                                                      .withMatcher("uid", propertyMatcher)
                                                      .withMatcher("customer.firstName", propertyMatcher)
                                                      .withMatcher("customer.lastName", propertyMatcher);
        Example<Order> orderExample = Example.of(order, exampleMatcher);
        return this.orderRepository.findAll(orderExample, pageable);
    }

    public Optional<Order> getOrderById(Long id) {
        return this.orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        this.orderRepository.deleteById(id);
    }

    public boolean isOrderExists(Long id) {
        return this.orderRepository.existsById(id);
    }

    public long getTotalCount() {
        return this.orderRepository.count();
    }
}
