package com.example.library.service.impl;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import com.example.library.repository.CustomerRepository;
import com.example.library.repository.RoleRepository;
import com.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setUsername(customerDto.getUsername());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(roleRepository.findByName("Customer")));

//        Customer custmerSave = repository.save(customer);
//        return mappedeDto(custmerSave);
        return repository.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Customer updateInfo(Customer customer) {
        Customer customerToUpdate = repository.findByUsername(customer.getUsername());
        customerToUpdate.setCity(customer.getCity());
        customerToUpdate.setAddress(customer.getAddress());
        customerToUpdate.setPhone(customer.getPhone());
        customerToUpdate.setCountry(customer.getCountry());

        return repository.save(customerToUpdate);
    }
//    CustomerDto mappedeDto(Customer customer){
//        CustomerDto customerDto = new CustomerDto();
//        customerDto.setUsername(customer.getUsername());
//        customerDto.setFirstName(customer.getFirstName());
//        customerDto.setLastName(customer.getLastName());
//        customerDto.setPassword(customer.getPassword());
//        return customerDto;
//    }
}
