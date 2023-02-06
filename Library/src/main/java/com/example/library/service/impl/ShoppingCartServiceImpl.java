package com.example.library.service.impl;

import com.example.library.dto.ProductDto;
import com.example.library.model.CartItem;
import com.example.library.model.Customer;
import com.example.library.model.Product;
import com.example.library.model.ShoppingCart;
import com.example.library.repository.CartItemRepository;
import com.example.library.repository.ShoppingCartRepository;
import com.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional

public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository itemRepository;

    @Override
    public ShoppingCart addToCart(Product product, int quantity, Customer customer) {

        ShoppingCart cart = customer.getShoppingCart();
        if (cart == null) {
            cart = new ShoppingCart();
        }
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = findCartItem(cartItems, product.getId());

        if (cartItems == null) {
            cartItems = new HashSet<>();
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getSellPrice());
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(quantity * product.getSellPrice());
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getSellPrice()));

                itemRepository.save(cartItem);
            }
        }
        cart.setCartItems(cartItems);

        int totalItems = totalItems(cart.getCartItems());
        double totalPrice = totalPrice(cart.getCartItems());

        cart.setTotalPrices(totalPrice);
        cart.setTotalItems(totalItems);
        cart.setCustomer(customer);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {

        ShoppingCart cart = customer.getShoppingCart();//Getting Cart from Customer
        Set<CartItem> cartItems = cart.getCartItems();//for it's Already Existed
        CartItem item = findCartItem(cartItems, product.getId());// Get the products for Updating

        item.setQuantity(quantity);
        item.setTotalPrice(quantity * product.getSellPrice());
        itemRepository.save(item);

        int totalItems = totalItems(cartItems);
        double totalPrice = totalPrice(cartItems);

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);


        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteItemInCart(Product product, Customer customer) {

        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItems();
        CartItem item = findCartItem(cartItems, product.getId());

        cartItems.remove(item);
        itemRepository.deleteItemFromCart(product.getId());

//      UPDATE THE SHOPPING CART
        double totalPrices = totalPrice(cartItems);
        int totalItems = totalItems(cartItems);

        cart.setCartItems(cartItems);
        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrices);

        return shoppingCartRepository.save(cart);
    }

    private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private int totalItems(Set<CartItem> cartItems) {
        int totalItems = 0;
        for (CartItem item : cartItems) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    private double totalPrice(Set<CartItem> cartItems) {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }


}
