package me.shamanov.oms.rest;

import me.shamanov.oms.dto.Orders;
import me.shamanov.oms.model.Order;
import me.shamanov.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderRestController {
    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = {"/", "/orders/json"}, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public Orders getOrdersAsJSON() {
        return this.getOrders();
    }

    @GetMapping(value = "/orders/xml", produces = {MimeTypeUtils.APPLICATION_XML_VALUE})
    public Orders getOrdersAsXML() {
        return this.getOrders();
    }

    private Orders getOrders() {
        List<Order> orderList = this.orderService.getAllOrders();
        return new Orders(orderList);
    }
}
