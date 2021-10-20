package me.shamanov.oms.repository;

import me.shamanov.oms.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c order by c.orders.size asc")
    Page<Customer> getAllCustomersOrderByOrdersSizeAsc(Pageable pageable);

    @Query("select c from Customer c order by c.orders.size desc")
    Page<Customer> getAllCustomersOrderByOrdersSizeDesc(Pageable pageable);

    @Query("select new map(c.id, max(c.orders.size)) from Customer c, Order o " +
            "where o.customer.id = c.id group by (c.id) order by max(c.orders.size) desc")
    List<Map<String, Long>> getCustomerWithMaxOrders(Pageable pageable);
}
