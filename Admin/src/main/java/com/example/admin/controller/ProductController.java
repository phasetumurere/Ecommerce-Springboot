package com.example.admin.controller;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.service.impl.CategoryServiceImpl;
import com.example.library.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        model.addAttribute("title", "Products");

        return "redirect:/products/0";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect/:login";
        }
        Page<ProductDto> products = productService.pageProducts(pageNo);
        model.addAttribute("title", "Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/search_result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo") int pageNo,
                                 @RequestParam("keyword") String keyword,
                                 Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Search Result");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products";
    }

    @GetMapping("/add_product")
    public String addProductForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Add new Product");
        model.addAttribute("produce", new ProductDto());
        model.addAttribute("button", "Save");
        return "add_product_form";
    }

    @PostMapping("/save_product")
    public String saveProduct(@ModelAttribute("produce") ProductDto productDto, Long id,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes ra) {
        try {
            if (id == null) {
                productService.save(imageProduct, productDto);
                ra.addFlashAttribute("success", "product" + productDto.getName() + " saved successfully");
            }
            if (id != null) {
                productService.update(imageProduct, productDto);
                ra.addFlashAttribute("success", "product" + productDto.getName() + " saved successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Server ERROR!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/update_product/{id}")
    public String edit(@PathVariable("id") Long id, Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Update Product");
        model.addAttribute("button", "Update");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("produce", productDto);
        return "add_product_form";

    }

    @RequestMapping(value = "/delete_product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            productService.deleteById(id);
            ra.addFlashAttribute("setDelete", "Product can't be Visible to the user");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Server Error");
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/enable_product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            productService.enableById(id);
            ra.addFlashAttribute("setEnabled", "Product now set to be visible to the user");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Server Error");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/remove_product/{id}")
    public String removeProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            productService.removeProduct(id);
            ra.addFlashAttribute("deleted", "Product Deleted Permanently");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "server Error!!");
        }
        return "redirect:/products/0";
    }
}
