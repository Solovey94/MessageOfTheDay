package com.solovey.messageoftheday.service.impl;

import com.solovey.messageoftheday.dto.CustomerDto;
import com.solovey.messageoftheday.dto.CustomerRequestDto;
import com.solovey.messageoftheday.dto.CustomerResponseDto;
import com.solovey.messageoftheday.exception.CustomException;
import com.solovey.messageoftheday.exception.NotFoundException;
import com.solovey.messageoftheday.model.Customer;
import com.solovey.messageoftheday.model.Message;
import com.solovey.messageoftheday.model.Role;
import com.solovey.messageoftheday.repository.CustomerRepository;
import com.solovey.messageoftheday.security.JwtTokenProvider;
import com.solovey.messageoftheday.service.CustomerService;
import com.solovey.messageoftheday.service.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            @Lazy MessageService messageService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.customerRepository = customerRepository;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, customerRepository.findByUsername(username).get().getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    @Override
    public Customer addCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = new Customer();
        customer.setUsername(customerRequestDto.getUsername());
        customer.setPassword(customerRequestDto.getPassword());
        customer.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_CLIENT)));
        BeanUtils.copyProperties(customerRequestDto, customer, "id");
        return customer;
    }


    @Transactional
    @Override
    public String signUp(Customer customer) {
        if (!customerRepository.existsByUsername(customer.getUsername())) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);
            return jwtTokenProvider.createToken(customer.getUsername(), customer.getRoles());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @Override
    public Customer whoAmI(HttpServletRequest req) {
        return customerRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req))).get();
    }


    @Transactional
    @Override
    public Customer updateCustomer(CustomerDto customerDto) {
        Customer customer = getCustomerById(customerDto.getId());
        BeanUtils.copyProperties(customerDto, customer, "id");
        return customer;
    }

    @Transactional
    @Override
    public CustomerDto update(CustomerDto customerDto) {
        return convertToDto(customerRepository.saveAndFlush(updateCustomer(customerDto)));
    }

    @Transactional
    @Override
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new NotFoundException("Not found element by id " + id.toString());
        }
    }

    @Transactional
    @Override
    public CustomerResponseDto findCustomerById(Long id) {
        return convertToResponseDto(getCustomerById(id));
    }


    @Transactional
    @Override
    public List<CustomerDto> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.size() > 0) {
            return convertToDto(customers);
        } else {
            throw new NotFoundException("Not found any elements");
        }
    }

    @Transactional
    @Override
    public CustomerResponseDto findCustomerByUsername(String username) {
        if (username == null) {
            throw new NotFoundException("Not found by username " + username.toString());
        }
        Optional<Customer> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            return convertToResponseDto(customer.get());
        } else {
            throw new NotFoundException("Not found by username " + username.toString());
        }
    }

    @Transactional
    @Override
    public Customer findByUsername(String username) {
        if (username == null) {
            throw new NotFoundException("Not found by username " + username.toString());
        }
        Optional<Customer> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new NotFoundException("Not found by username " + username.toString());
        }
    }

    @Transactional
    @Override
    public void deleteCustomerById(Long id) {
        Customer customer = getCustomerById(id);
        Set<Message> messages = customer.getMessages();
        for (Message message : messages) {
            messageService.deleteMessageById(message.getId());
        }
        customerRepository.save(customer);
        customerRepository.delete(customer);
    }

    @Override
    public CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Override
    public CustomerRequestDto convertToRequestDto(Customer customer) {
        CustomerRequestDto customerDto = new CustomerRequestDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Override
    public CustomerResponseDto convertToResponseDto(Customer customer) {
        CustomerResponseDto customerDto = new CustomerResponseDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Override
    public List<CustomerDto> convertToDto(Iterable<Customer> customers) {
        List<CustomerDto> result = new ArrayList<>();
        for (Customer customer : customers) {
            result.add(convertToDto(customer));
        }
        return result;
    }

}
