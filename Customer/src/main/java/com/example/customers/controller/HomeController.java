package com.example.customers.controller;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;
import com.example.library.service.impl.CategoryServiceImpl;
import com.example.library.service.impl.CustomerServiceImpl;
import com.example.library.service.impl.ProductServiceImpl;
import com.example.library.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

import static java.lang.Integer.parseInt;

@Controller
public class HomeController {
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model, Principal principal, HttpSession session) {
        if (principal != null) {
            session.setAttribute("username", principal.getName());

            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            ShoppingCart cart = customer.getShoppingCart();
            if (cart == null || cart.toString().isEmpty()) {
                model.addAttribute("noItem", "0");
                return "redirect:/home";
            }
            session.setAttribute("totalItems", cart.getTotalItems());
        } else {
            session.removeAttribute("username");
        }
        model.addAttribute("title", "Xhirwa SHOP");
        model.addAttribute("page", "Menu");
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session, Principal principal) {
        if (principal != null) {
            session.setAttribute("username", principal.getName());

            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            ShoppingCart cart = customer.getShoppingCart();
            if (cart == null || cart.toString().isEmpty()) {
//                    model.addAttribute("noItem", "0");
                return "redirect:/products";
            }
            session.setAttribute("totalItems", cart.getTotalItems());
        } else {
            session.removeAttribute("username");
        }
        List<Category> categoriesList = categoryService.findAll();
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("categories", categoriesList);
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "All the Product");
        model.addAttribute("page", "Menu");
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "index";
    }
}
