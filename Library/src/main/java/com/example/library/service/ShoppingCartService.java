package com.example.library.service;

import com.example.library.dto.ProductDto;
import com.example.library.model.CartItem;
import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addToCart(Product product, int quantity, Customer customer);

    ShoppingCart updateItemInCart(Product product, int quantity, Customer customer);

    ShoppingCart deleteItemInCart(Product product, Customer customer);
}
