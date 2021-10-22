package me.shamanov.oms.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/orders")
@Api(tags = {"Order Controller"})
public class OrderRestController {
    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Orders as JSON", description = "Get all orders in JSON format")
    @GetMapping(value = {"/", "/json"}, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public Orders getOrdersAsJSON() {
        return this.getOrders();
    }

    @Operation(summary = "Orders as XML", description = "Get all orders in XML format")
    @GetMapping(value = "/xml", produces = {MimeTypeUtils.APPLICATION_XML_VALUE})
    public Orders getOrdersAsXML() {
        return this.getOrders();
    }

    private Orders getOrders() {
        List<Order> orderList = this.orderService.getAllOrders();
        return new Orders(orderList);
    }
}
