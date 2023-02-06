package com.example.customers.controller;

import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;
import com.example.library.service.impl.CustomerServiceImpl;
import com.example.library.service.impl.ProductServiceImpl;
import com.example.library.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private ProductServiceImpl productService;


    @GetMapping("/cart")
    public String cart(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        if (shoppingCart == null || shoppingCart.toString().isEmpty()) {
            model.addAttribute("check", "No Item in your cart " + username);
            return "cart";
        }
        session.setAttribute("shoppingCart", shoppingCart.getTotalItems());
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("title", username + "'s Cart");
        model.addAttribute("page", username + "'s Cart");
        double subTotal = shoppingCart.getTotalPrices();
        double tax = subTotal * 18 / 100;
        model.addAttribute("tax", tax);
        model.addAttribute("subTotal", subTotal);

        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);


        return "cart";
    }

    @PostMapping("/add_to_cart")
    public String addItemToCart(
            @RequestParam("id") Long id,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Model model,
            Principal principal,
            HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }
        Product product = productService.getCartProductById(id);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

        ShoppingCart cart = shoppingCartService.addToCart(product, quantity, customer);
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/update_cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("quantity") int quantity,
                             @RequestParam("id") Long id,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Product product = productService.getCartProductById(id);

            ShoppingCart cart = shoppingCartService.updateItemInCart(product, quantity, customer);
            model.addAttribute("ShoppingCart", cart); //Must be the same as in /cart :Above
            return "redirect:/cart";
        }
    }

    @RequestMapping(value = "/update_cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteFromCart(@RequestParam("id") Long id, int quantity,
                                 Model model,
                                 Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Product product = productService.getCartProductById(id);
            try {
//                model.addAttribute("ShoppingCart", product);
                ShoppingCart cart = shoppingCartService.deleteItemInCart(product, customer);

//                shoppingCartService.removeFromCart(product.getId());
                model.addAttribute("ShoppingCart", cart); //Must be the same as in /cart :Above

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "redirect:/cart";
        }
    }
}
