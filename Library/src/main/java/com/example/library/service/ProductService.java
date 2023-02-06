package com.example.library.service;

import com.example.library.dto.ProductDto;
import com.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    Product save(MultipartFile imageProduct, ProductDto productDto);

    Product update(MultipartFile imageProduct, ProductDto productDto);

    void deleteById(Long id);

    void enableById(Long id);

    ProductDto getById(Long id);

    void removeProduct(Long id);

    Page<ProductDto> pageProducts(int pageNo);

    Page<ProductDto> searchProducts(int pageNo, String keyword);

//    Customer

    List<Product> getAllProducts();

    List<Product> listViewProduct();

}
