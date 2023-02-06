package com.example.customers.controller;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import com.example.library.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        session.removeAttribute("message");
        model.addAttribute("title", "Xhirwa SHOP Login");
        model.addAttribute("customerDto", new CustomerDto());
        return "signup";
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
        session.removeAttribute("message");
        model.addAttribute("title", "Xhirwa SHOP Register");
        model.addAttribute("customerDto", new CustomerDto());
        return "signup";
    }


    @PostMapping("/do_register")
    public String addCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                              BindingResult result,
                              Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "signup";
            }
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username);

            if (customer != null) {
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("warning", "Your Email already registered ");
                System.out.println(username + "Already registered");
                return "signup";
            }
            if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                model.addAttribute("customerDto", customerDto);
                customerService.save(customerDto);
                System.out.println(customerDto.getFirstName() + " registered");
                model.addAttribute("message", "Registered Succesifully as " + customerDto.getUsername());

            } else {
                model.addAttribute("customerDto", customerDto);
                System.out.println(customerDto.getPassword() + " ⫩ " + customerDto.getRepeatPassword());
                model.addAttribute("warning", " Password not the same");
                return "signup";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server Error Try Again leter");
            model.addAttribute("danger", "Kindly bear with us, It's Server error Back Soon!");

        }

        return "signup";
    }
}
