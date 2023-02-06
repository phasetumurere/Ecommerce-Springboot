package com.example.customers.controller;

import com.example.library.dto.CategoryDto;
import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Product;
import com.example.library.service.CategoryService;
import com.example.library.service.ProductService;
import com.example.library.service.impl.CategoryServiceImpl;
import com.example.library.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<Product> activatedProducts = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProduct();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        model.addAttribute("listViewProduct", listViewProducts);
        model.addAttribute("products", activatedProducts);
        model.addAttribute("page", "Shop Details");
        model.addAttribute("title", "xHirwa All Products");
        model.addAttribute("categories", categoryDtoList);
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "shop";
    }

    @GetMapping("/product_details/{id}")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.getProductById(id);
        Long cat_id = product.getCategory().getId();
        List<Product> productOfSameCategory = productService.similarCategory(cat_id);
        model.addAttribute("product", product);
        model.addAttribute("title", "Product Details");
        model.addAttribute("page", product.getName() + " Information");
        model.addAttribute("sameCategory", productOfSameCategory);
//        For displaying Same in Footer
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "product-detail";
    }

    @GetMapping("/products_by_seller/{id}")
    public String getProductsByItsCategory(@PathVariable("id") Long categorId, Model model) {
        Category category = categoryService.findById(categorId);
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        List<Product> productsInCategory = productService.getProductsInCategory(categorId);

        model.addAttribute("category", category);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("products", productsInCategory);
        model.addAttribute("title", "all Products of " + category.getName());
        model.addAttribute("page", " Products in " + category.getName());

        //        For displaying Same in Footer
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "products_in_category";
    }

    @GetMapping("/filter_highPrices")
    public String filterHighPrices(Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        List<Product> productList = productService.filterHighPrices();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtos", categoryDtoList);
        model.addAttribute("products", productList);
        model.addAttribute("title", "sorted from expensive");
        model.addAttribute("page", "High Prices to Low");

        //        For displaying Same in Footer
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "filter_high";
    }

    @GetMapping("/filter_lowPrices")
    public String filterLowPrices(Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
        List<Product> productList = productService.filterLowPrices();
        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtos", categoryDtoList);
        model.addAttribute("products", productList);
        model.addAttribute("title", "sorted from cheapest");
        model.addAttribute("page", "Low Prices to High");

        //        For displaying Same in Footer
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "filter_low";
    }

}
