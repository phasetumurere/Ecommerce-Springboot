package com.example.customers.controller;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.service.impl.CategoryServiceImpl;
import com.example.library.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    private ProductServiceImpl productService;

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("title", "Xhirwa SHOP");
        model.addAttribute("page", "Menu");
        return "product-detail";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Category> categoriesList = categoryService.findAll();
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("categories", categoriesList);
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Xhirwa SHOP");
        model.addAttribute("page", "Menu");
        return "index";
    }
}
