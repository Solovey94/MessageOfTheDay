package com.solovey.messageoftheday.repository;

import com.solovey.messageoftheday.model.Customer;
import com.solovey.messageoftheday.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByText(String text);

    List<Customer> findByCustomerId(Long customerId);

}
