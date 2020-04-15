package com.solovey.messageoftheday.repository;

import com.solovey.messageoftheday.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAll();

    List<Customer> findByUsername(String username);

}
