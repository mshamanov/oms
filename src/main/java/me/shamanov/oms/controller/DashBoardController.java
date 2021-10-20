package me.shamanov.oms.controller;

import me.shamanov.oms.model.Customer;
import me.shamanov.oms.service.CustomerService;
import me.shamanov.oms.service.OrderService;
import me.shamanov.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class DashBoardController {
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public DashBoardController(CustomerService customerService, ProductService productService,
                               OrderService orderService) {
        this.customerService = customerService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getDashBoardPage(Model model) {
        long totalCustomers = this.customerService.getTotalCount();
        long totalOrders = this.orderService.getTotalCount();
        long totalProducts = this.productService.getTotalCount();

        List<Map<String, Long>> result = this.customerService.getCustomerWithMaxOrders(PageRequest.of(0, 1));

        Customer customerWithMaxOrders = null;
        if (result.size() == 1) {
            final Map<String, Long> idMap = result.get(0);
            if (idMap.get("0") != null) {
                customerWithMaxOrders = this.customerService.getCustomerById(idMap.get("0"))
                                                            .orElseThrow(() -> new ResponseStatusException(
                                                                    HttpStatus.NOT_FOUND, "Customer not found"));
            }
        }

        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("maxOrdered", customerWithMaxOrders);
        return "dashboard";
    }
}
