package com.example.customers.controller;

import com.example.library.model.City;
import com.example.library.model.Customer;
import com.example.library.service.CityService;
import com.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {
    @Autowired
    private CityService cityService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/my_account")
    public String myAccountHome(Model model, Principal principal) {
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("page", "Account Information");
        return "account";
    }
}
