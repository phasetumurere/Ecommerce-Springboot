package com.example.customers.controller;

import com.example.library.model.Product;
import com.example.library.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<Product> activatedProducts = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProduct();
        model.addAttribute("listViewProduct", listViewProducts);
        model.addAttribute("products", activatedProducts);
        model.addAttribute("page", "Shop Details");
        return "shop";
    }
}
