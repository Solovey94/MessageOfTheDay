package com.solovey.messageoftheday.service;

import com.solovey.messageoftheday.dto.CustomerDto;
import com.solovey.messageoftheday.dto.CustomerRequestDto;
import com.solovey.messageoftheday.dto.CustomerResponseDto;
import com.solovey.messageoftheday.model.Customer;


import java.util.List;


public interface CustomerService{

    String login(String username, String password);

    String signUp(Customer customer);

    Customer addCustomer(CustomerRequestDto customerRequestDto);

    Customer updateCustomer(CustomerDto customerDto);

    CustomerDto update(CustomerDto customerDto);

    Customer getCustomerById(Long id);

    CustomerResponseDto findCustomerById(Long id);

    List<CustomerDto> findAllCustomers();

    CustomerResponseDto findCustomerByUsername(String username);

    void deleteCustomerById(Long id);

    CustomerDto convertToDto(Customer customer);

    CustomerRequestDto convertToRequestDto(Customer customer);

    CustomerResponseDto convertToResponseDto(Customer customer);

    List<CustomerDto> convertToDto(Iterable<Customer> customers);

}
