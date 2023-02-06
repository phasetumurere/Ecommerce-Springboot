package com.example.admin.controller;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "login_form";
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
        session.removeAttribute("message");
        model.addAttribute("adminDto", new AdminDto());
        model.addAttribute("title", "Register");
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @RequestMapping("/index")
    public String homePage(Model model) {

        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "index";
    }

    @PostMapping("/register_new")
    public String addAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                           BindingResult result,
                           Model model) {

        try {

            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                result.toString();
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);

            if (admin != null) {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("message", "Your email Registered Already!");
                System.out.println("the Username Arleady registered");
                return "register";
            }
            if (adminDto.getRepeatPassword().equals(adminDto.getPassword())) {
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                model.addAttribute("adminDto", adminDto);
                adminService.save(adminDto);
                System.out.println("Admin saved");
                model.addAttribute("message", "Admin " + adminDto.getFirstName() + " Registered Successfully!");
            } else {
                model.addAttribute("adminDto", adminDto);
                System.out.println(adminDto.getPassword());
                System.out.println(adminDto.getRepeatPassword());
                model.addAttribute("message", "Password is not the same");
                return "register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("server Errors Try again leter");
            model.addAttribute("message", "Server Error, Please try again leter!");
        }
        return "register";
    }
}