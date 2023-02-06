package com.example.library.service;

import com.example.library.model.Notification;
import com.example.library.model.Order;
import com.example.library.model.ShoppingCart;

public interface OrderService {
    void saveOrder(ShoppingCart cart);

    void acceptOrder(Long id);

    void cancelOrder(Long id);

    Notification orderNotification();

    Double sumAllPrices();
}
