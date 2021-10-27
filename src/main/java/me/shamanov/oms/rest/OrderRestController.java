package me.shamanov.oms.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import me.shamanov.oms.dto.Orders;
import me.shamanov.oms.model.Order;
import me.shamanov.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = {"Orders"})
public class OrderRestController {
    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Orders as JSON/XML", description = "Get all orders in JSON/XML format")
    @GetMapping(value = "/orders", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Orders getOrders() {
        List<Order> orderList = this.orderService.getAllOrders();
        return new Orders(orderList);
    }
}
