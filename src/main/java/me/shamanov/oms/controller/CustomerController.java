package me.shamanov.oms.controller;

import me.shamanov.oms.model.Customer;
import me.shamanov.oms.model.Order;
import me.shamanov.oms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping
@SessionAttributes("currentOrder")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String getCustomersPage(@RequestParam(value = "id", required = false) Long id,
                                   @RequestParam(value = "search", required = false) String search,
                                   @RequestParam(defaultValue = "firstName") String sort,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(defaultValue = "asc") String direction,
                                   Model model) {
        if (id != null) {
            Customer customer = this.customerService.getCustomerById(id).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found"));
            model.addAttribute("customer", customer);
            return "customer-info";
        }

        Page<Customer> customerPage;
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        if (search != null && !search.isBlank()) {
            customerPage = this.customerService.getCustomersBySearchValue(search,
                    pageRequest.withSort(Sort.Direction.fromString(direction), sort));
        } else {
            customerPage = this.customerService.getAllCustomers(
                    pageRequest.withSort(Sort.Direction.fromString(direction), sort));
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("customers", customerPage.getContent());
        return "customers";
    }

    @GetMapping("/addCustomer")
    public String getAddCustomerPage(Model model) {
        model.addAttribute("pageTitle", "Adding new customer");
        model.addAttribute("customer", new Customer());
        return "customer-edit";
    }

    @GetMapping("/updateCustomer")
    public String getUpdateCustomerPage(@RequestParam Long id, Model model) {
        Customer existingCustomer =
                this.customerService.getCustomerById(id).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));
        model.addAttribute("pageTitle", "Updating customer");
        model.addAttribute("customer", existingCustomer);
        return "customer-edit";
    }

    @GetMapping("/deleteCustomer")
    public String deleteCustomer(@RequestParam Long id, Model model) {
        if (!this.customerService.isCustomerExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        Object order = model.getAttribute("currentOrder");
        String clearSessionCustomerId = "";

        if (order != null) {
            Order currentOrder = (Order) order;
            if (currentOrder.getCustomer() != null) {
                Long currentCustomerId = currentOrder.getCustomer().getId();
                if (currentCustomerId != null && currentCustomerId.equals(id)) {
                    clearSessionCustomerId = "?clearCustomerId=" + currentOrder.getCustomer().getId();
                    model.addAttribute("currentOrder", null);
                }
            }
        }

        this.customerService.deleteCustomerById(id);
        return "redirect:/customers" + clearSessionCustomerId;
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@Valid @ModelAttribute Customer customer, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("pageTitle", (customer.getId() != null ? "Updating" : "Adding") + " new customer");
            return "customer-edit";
        } else {
            this.customerService.saveCustomer(customer);
            return "redirect:/customers";
        }
    }
}
