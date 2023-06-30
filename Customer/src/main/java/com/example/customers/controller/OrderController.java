package com.example.customers.controller;

import com.example.library.model.*;
import com.example.library.service.CustomerService;
import com.example.library.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/checkout")
    public String checkOut(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer.getPhone() == null
                || customer.getAddress() == null
                || customer.getCity() == null
                || customer.getCountry() == null) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout");
            return "account";
        } else {
            model.addAttribute("customer", customer);
            ShoppingCart cart = customer.getShoppingCart();
            double subTotal = cart.getTotalPrices();
            double tax = subTotal * 18 / 100;
            model.addAttribute("cart", cart);
            model.addAttribute("subTotal", subTotal);
            model.addAttribute("tax", tax);
            model.addAttribute("page", "Checkout");
            model.addAttribute("title", "Checkout");
        }
        return "checkout";
    }

    @GetMapping("add_notification")
    public String notification(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer.getPhone() == null
                || customer.getAddress() == null
                || customer.getCity() == null
                || customer.getCountry() == null) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout");
            return "account";
        } else {
            model.addAttribute("customer", customer);

            List<OrderDetails> orderDetails = customer.getOrderDetails();

            model.addAttribute("details", orderDetails);

            List<Order> orders = customer.getOrders();
            List<Order> allOrders = customer.getOrders();
            double totalPrices = orderService.sumAllPrices();
            double tax = (totalPrices * 18) / 100;
            double all = totalPrices + tax;

            model.addAttribute("order", orders);
            model.addAttribute("total", totalPrices);
            model.addAttribute("tax", tax);
            model.addAttribute("notification", orderService.orderNotification());
            model.addAttribute("page", "Check Order");
            model.addAttribute("title", "Check Order");

        }

        return "confirm_order";
    }


    @RequestMapping(value = "/update_info", method = {RequestMethod.GET, RequestMethod.PUT})
    public String updateInformation(@ModelAttribute("customer") Customer customer,
                                    Principal principal,
                                    Model model,
                                    RedirectAttributes ra) {
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customerUpdated = customerService.updateInfo(customer);
        ra.addFlashAttribute("customer", customerUpdated);

        return "redirect:/checkout";
    }

    @GetMapping("/order")
    public String makeOrder(Principal principal, Model model, OrderDetails order, Order orders) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        List<Order> orderList = customer.getOrders();
//        Order orderList = customer.getOrders();
        model.addAttribute("orders", orderList);
        List<OrderDetails> orderDetailsList = customer.getOrderDetails();
        model.addAttribute("orderDetails", orderDetailsList);
        Date today = new Date();
        model.addAttribute("delivery", today);
        model.addAttribute("title", customer.getFirstName() + "'s Order");
        model.addAttribute("page", "Order");
        return "order";
    }

    @GetMapping("/save_order")
    public String saveOrder(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getShoppingCart();
        orderService.saveOrder(cart);
        return "redirect:/order";
    }

    @GetMapping("remove_order/{id}")
    public String removeOrder(@PathVariable("id") Long id,
                              OrderDetails order,
                              Principal principal,
                              Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        orderService.cancelOrder(order.getId());
        return "redirect:/order";
    }

    @GetMapping("confirm_order/{id}")
    public String acceptOrder(@PathVariable("id") Long id,
                              Principal principal,
                              Model model,
                              Order order) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        orderService.acceptOrder(order.getId());
        return "redirect:/order";
    }

    @GetMapping("/download_invoice")
    public String sendEmail(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        if (customer.getPhone() == null
                || customer.getAddress() == null
                || customer.getCity() == null
                || customer.getCountry() == null) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout");
            return "account";
        } else {
            model.addAttribute("customer", customer);

            List<OrderDetails> orderDetails = customer.getOrderDetails();

            model.addAttribute("details", orderDetails);

            List<Order> orders = customer.getOrders();
            List<Order> allOrders = customer.getOrders();
            Double totalPrices = orderService.sumAllPrices();
            Double tax = (totalPrices * 18) / 100;
            Double all = totalPrices + tax;

            model.addAttribute("order", orders);
            model.addAttribute("total", totalPrices);
            model.addAttribute("tax", tax);
            model.addAttribute("page", "Invoice");
            model.addAttribute("title", "Invoice");
            model.addAttribute("notification", orderService.orderNotification());
            Notification message = new Notification();
            message.setClientEmail(customer.getUsername());
//            message.setProductInformation(orders);
            message.setComment("Hello " + username + " Thank you very much for shopping with xHirwa" +
                    "make sure that you send " + all + " to our MoMo account which is 0785309380" +
                    "and we are going to deliver all your commodities after reaching at" +
                    customer.getAddress() + " we will call you at " + customer.getPhone() + " for " +
                    "reaching at your exact Location 😋");
            model.addAttribute("message", message);

        }
        return "contact-us";
    }

}
