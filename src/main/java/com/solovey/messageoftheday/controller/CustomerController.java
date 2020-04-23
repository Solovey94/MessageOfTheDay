package com.solovey.messageoftheday.controller;

import com.solovey.messageoftheday.dto.CustomerDto;
import com.solovey.messageoftheday.dto.CustomerRequestDto;
import com.solovey.messageoftheday.dto.CustomerResponseDto;
import com.solovey.messageoftheday.model.Customer;
import com.solovey.messageoftheday.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody CustomerRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            String password = requestDto.getPassword();
            String token = customerService.login(username, password);
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody CustomerRequestDto customerRequestDto) {
        try {
            String username = customerRequestDto.getUsername();
            String token = customerService.signUp(modelMapper.map(customerService.addCustomer(customerRequestDto), Customer.class));
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


    @PutMapping("/{id}")
    public CustomerDto updateCustomer(
            @PathVariable Long id,
            @Validated @RequestBody CustomerDto customerDto
    ) {
        customerDto.setId(id);
        return customerService.update(customerDto);
    }


    @GetMapping
    public List<CustomerDto> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerResponseDto findCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }


    @GetMapping("/name/{username}")
    public CustomerResponseDto findCustomerByUsername(@PathVariable String username) {
        return customerService.findCustomerByUsername(username);
    }


    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }

}
