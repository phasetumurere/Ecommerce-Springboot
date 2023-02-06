package com.example.library.service;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer updateInfo(Customer customer);
}
