package com.example.library.service.impl;

import com.example.library.model.*;
import com.example.library.repository.*;
import com.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void saveOrder(ShoppingCart cart) {
        Order order = new Order();
        order.setStatus("PENDING");
        Date today = new Date();
        order.setDeliveryDate(new Date(today.getTime() + (1000 * 60 * 60 * 24)));
        order.setCustomer(cart.getCustomer());
        order.setTotalPrice(cart.getTotalPrices());
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            orderDetails.setUnitPrice(item.getProduct().getSellPrice());
            orderDetails.setQuantity(item.getQuantity());
            orderDetails.setProduct(item.getProduct());
            orderDetails.setCustomer(item.getCart().getCustomer());
            orderDetailsRepository.save(orderDetails);
            orderDetailsList.add(orderDetails);
            cartItemRepository.delete(item);
        }
        order.setOrderDetailsList(orderDetailsList);
        cart.setCartItems(new HashSet<>());
        cart.setTotalPrices(0);
        cart.setTotalItems(0);

        shoppingCartRepository.save(cart);
        orderRepository.save(order);
    }

    @Override
    public void acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setOrderDate(new Date());
        order.setStatus("Delivering");
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
//        orderRepository.deleteById(id);
        orderDetailsRepository.deleteById(id);
    }

    @Override
    public Notification orderNotification() {

        Notification notification = new Notification();
        notification.setName("new Order");
        return notificationRepository.save(notification);
    }

    @Override
    public Double sumAllPrices() {
        List<Double> prices = orderRepository.getAllPrices();
        return prices.stream().mapToDouble(Double::doubleValue).sum();
    }
}
