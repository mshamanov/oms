package me.shamanov.oms.controller;

import me.shamanov.oms.model.Customer;
import me.shamanov.oms.model.Order;
import me.shamanov.oms.model.OrderItem;
import me.shamanov.oms.model.Product;
import me.shamanov.oms.service.CustomerService;
import me.shamanov.oms.service.OrderService;
import me.shamanov.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Controller
@RequestMapping
@SessionAttributes("currentOrder")
public class OrderController {
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;

    @ModelAttribute("currentOrder")
    public Order currentOrder() {
        return new Order();
    }

    @Autowired
    public OrderController(CustomerService customerService, ProductService productService,
                           OrderService orderService) {
        this.customerService = customerService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrdersPage(@RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "search", required = false) String search,
                                @RequestParam(defaultValue = "createdOn") String sort,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int size,
                                @RequestParam(defaultValue = "asc") String direction,
                                Model model) {
        if (id != null) {
            Order order = this.orderService.getOrderById(id).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order not found"));
            model.addAttribute("order", order);
            return "order-info";
        }

        if (sort.equalsIgnoreCase("customer")) {
            sort = "customer.firstName";
        }

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(direction), sort).ignoreCase();
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortOrder));

        Page<Order> orderPage;
        if (search != null && !search.isBlank()) {
            orderPage = this.orderService.getOrdersBySearchValue(search, pageRequest);
        } else {
            orderPage = this.orderService.getAllOrders(pageRequest);
        }

        List<Order> orders = orderPage.getContent();
        Optional<BigDecimal> totalOrdersPrice = orders.stream()
                                                      .flatMap(order -> order.getOrderItems().stream())
                                                      .map(OrderItem::getTotalPrice)
                                                      .reduce(BigDecimal::add);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalOrdersPrice", totalOrdersPrice.orElse(BigDecimal.ZERO));
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/createOrder")
    public String getCreateOrderPage(@RequestParam(value = "customerId") Long id,
                                     @RequestParam(value = "search", required = false) String search,
                                     @RequestParam(defaultValue = "name") String sort,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(defaultValue = "asc") String direction,
                                     @ModelAttribute("currentOrder") Order order, Model model) {
        if (order.getCustomer() == null || !order.getCustomer().getId().equals(id)) {
            Customer customer = this.customerService.getCustomerById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
            order.setId(null);
            order.setUid(null);
            order.setCustomer(customer);
        }

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(new Product());
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(direction), sort).ignoreCase();
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortOrder));

        Page<Product> productPage;
        if (search != null && !search.isBlank()) {
            productPage = this.productService.getProductsBySearchValue(search, pageRequest);
        } else {
            productPage = this.productService.getAllProducts(pageRequest);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("products", productPage.getContent());
        return "create-order";
    }

    @PostMapping("/saveCart")
    public String saveCart(@Valid @ModelAttribute("currentOrder") Order order, Errors errors,
                           SessionStatus sessionStatus, Model model) {
        if (errors.hasErrors()) {
            return "cart";
        } else {
            this.recalculateOrder(order);

            if (order.getOrderItems().size() < 1) {
                return "cart";
            }

            Order saved = this.orderService.saveOrder(order);
            sessionStatus.setComplete();
            model.addAttribute("order", saved);

            return "order-result";
        }
    }

    @GetMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("id") Long id,
                              @RequestParam(defaultValue = "/orders") String redirectUrl,
                              Model model) {
        if (!this.orderService.isOrderExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        Object order = model.getAttribute("currentOrder");
        String clearSessionCustomerId = "";

        if (order != null) {
            Order currentOrder = (Order) order;
            Long currentOrderId = currentOrder.getId();
            if (currentOrderId != null && currentOrderId.equals(id)) {
                if (currentOrder.getCustomer() != null && currentOrder.getCustomer().getId() != null) {
                    clearSessionCustomerId = (redirectUrl.contains("?") ? "&" : "?") + "clearCustomerId=" +
                            currentOrder.getCustomer().getId();
                    model.addAttribute("currentOrder", null);
                }
            }
        }

        this.orderService.deleteOrderById(id);
        return "redirect:" + redirectUrl + clearSessionCustomerId;
    }

    @GetMapping("/updateOrder")
    public String getUpdateOrderPage(@ModelAttribute("currentOrder") Order order, @RequestParam("id") Long id,
                                     Model model) {
        Order existingOrder = this.orderService.getOrderById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.setId(existingOrder.getId());
        order.setUid(existingOrder.getUid());
        order.setCustomer(existingOrder.getCustomer());
        order.setOrderItems(existingOrder.getOrderItems());
        order.setCreatedOn(existingOrder.getCreatedOn());
        model.addAttribute("currentOrder", order);
        return "redirect:/showCart";
    }

    @PostMapping("/showCart")
    public String getShowCartPage(@ModelAttribute("currentOrder") Order order) {
        if (order.getCustomer() == null) {
            return "redirect:/";
        }

        this.recalculateOrder(order);

        return "cart";
    }

    @GetMapping("/showCart")
    public String getShowCartPage(@ModelAttribute("currentOrder") Order order,
                                  @RequestParam(required = false) Long deleteId) {
        if (order.getCustomer() == null) {
            return "redirect:/";
        }

        if (deleteId != null) {
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.removeIf(item -> item.getProduct().getId().equals(deleteId));
        }

        return "cart";
    }

    private void recalculateOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.removeIf(
                item -> item.getProduct() == null || item.getProduct().getId() == null || item.getAmount() < 1);
        Supplier<ResponseStatusException> exceptionSupplier =
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");

        for (OrderItem orderItem : orderItems) {
            Long productId = orderItem.getProduct().getId();
            Product dbProduct = this.productService.getProductById(productId).orElseThrow(exceptionSupplier);
            orderItem.setProduct(dbProduct);
            orderItem.setPrice(dbProduct.getPrice());
            orderItem.setTotalPrice(dbProduct.getPrice().multiply(BigDecimal.valueOf(orderItem.getAmount())));
            orderItem.setOrder(order);
        }
    }
}
