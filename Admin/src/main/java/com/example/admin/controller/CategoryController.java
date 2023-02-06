package com.example.admin.controller;

import com.example.library.model.Category;
import com.example.library.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/category")
    public String categories(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("title", "Categories");
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("categoryNew", new Category());
        return "categories";
    }


    @PostMapping("/add_category")
    public String save(@ModelAttribute("categoryNew") Category category, RedirectAttributes ra) {
        try {
            categoryService.save(category);
            ra.addFlashAttribute("success", "Category " + category.getName() + " Saved Successfully");
            System.out.println("Category " + category.getName() + " Added Succesifully!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Category " + category.getName() + " Already Exist");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Server Error!");
            System.out.println("Failed");
        }
        return "redirect:/category";
    }

    @RequestMapping(value = "/findById", method = {RequestMethod.GET, RequestMethod.PUT})
    @ResponseBody
    public Category findById(Long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/update_category")
    public String updateCategory(Category category, RedirectAttributes ra) {
        try {
            categoryService.update(category);
            ra.addFlashAttribute("success", "Category " + category.getName() + " Updated Successfully!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Category " + category.getName() + " Failed to Update because of Duplicated name");
        } catch (Exception e) {
            ra.addFlashAttribute("failed", "Server Error!");
        }

        return "redirect:/category";
    }

    @RequestMapping(value = "/delete_category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteCAtegory(Long id, RedirectAttributes ra) {

        try {
            categoryService.deleteById(id);
            ra.addFlashAttribute("desabled", "Category Desabled!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("unDesabled", "Server Error");
        }
        return "redirect:/category";
    }

    @RequestMapping(value = "/enable_category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableCategory(Long id, RedirectAttributes ra, Category category) {

        try {
            categoryService.enabledById(id);
            ra.addFlashAttribute("enabled", "Category Enabled!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("unDesabled", "Server Error");
        }
        return "redirect:/category";
    }

    @GetMapping("/delete_cat")
    public String deleteCategory(Long id, RedirectAttributes ra) {
        try {
            categoryService.delete(id);
            ra.addFlashAttribute("success", "Category Deleted");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("failed", "Server Error!");
        }
        return "redirect:/category";
    }

}
