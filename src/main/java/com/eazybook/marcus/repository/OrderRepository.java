package com.eazybook.marcus.repository;

import com.eazybook.marcus.entity.Customer;
import com.eazybook.marcus.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Fetch order for a customer, sorted by creation date in descending data order
    //    List<Order> findByCustomerOrderByCreatedAtDesc(Customer customer);
    // SELECT * FROM orders WHERE customer_id = ? ORDER BY created_at DESC;

    //    List<Order> findByOrderStatus(String orderStatus);

    @Query("SELECT o FROM Order o WHERE o.customer=:customer ORDER BY o.createdAt DESC")
    List<Order> findOrderByCustomer(@Param("customer") Customer customer);

    @Query("SELECT o FROM Order o WHERE o.orderStatus=?1")
    List<Order> findOrderByStatus(String orderStatus);

    // third way: NativeSqlQuery

    @Query(value = "SELECT * FROM orders o WHERE o.customer_id=:customerId ORDER BY o.created_at DESC"
            , nativeQuery = true)
    List<Order> findOrderByCustomerWithNativeQuery(@Param("customerId") Long customerId);

    @Query(value = "SELECT * FROM orders o WHERE o.order_status=?1", nativeQuery = true)
    List<Order> findOrderByStatusWithNativeQuery(String orderStatus);

    // JPQL QUery This One
    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.orderStatus=:orderStatus, o.updatedAt=CURRENT_TIMESTAMP, o.updatedBy=:updatedBy WHERE  o.orderId=:orderId")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatus") String orderStatus, @Param("updatedBy") String updatedBy);
}