package com.eazybook.marcus.repository;

import com.eazybook.marcus.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByName(String name);

    Optional<Customer> findByEmailOrMobileNumber(String email, String mobileNumber);

    boolean existsByRoles_Name(String name);
}