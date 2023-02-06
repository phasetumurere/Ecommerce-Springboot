package com.example.library.service.impl;


import com.example.library.dto.ProductDto;
import com.example.library.model.Product;
import com.example.library.repository.ProductRepository;
import com.example.library.service.ProductService;
import com.example.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private ImageUpload imageUpload;

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product product = new Product();
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                if (imageUpload.uploadImage(imageProduct)) {
                    System.out.println("Image Uploaded");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setCategory(productDto.getCategory());
            product.setDescription(productDto.getDescription());
            product.setCostPrice(productDto.getCostPrice());
            product.setSellPrice(productDto.getSellPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setIs_activated(true);
            product.setIs_deleted(false);
            return repository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product product = repository.getById(productDto.getId());
            if (imageProduct == null) {
                product.setImage(product.getImage());

            } else {
                if (imageUpload.checkExisted(imageProduct) == false) {
//                    System.out.println("Upload to folder");
                    imageUpload.uploadImage(imageProduct);
                }
                System.out.println("Image Exists");
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setSellPrice(productDto.getSellPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());

//            return product;
            return repository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deleteById(Long id) {
        Product product = repository.getById(id);
        product.setIs_deleted(true);
        product.setIs_activated(false);
        repository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product = repository.getById(id);
        product.setIs_activated(true);
        product.setIs_deleted(false);
        repository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = repository.getById(id);
        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setImage(product.getImage());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setSellPrice(product.getSellPrice());
        productDto.setCategory(product.getCategory());
        productDto.setIs_deleted(product.getIs_deleted());
        productDto.setIs_activated(product.getIs_activated());

        return productDto;
    }

    @Override
    public void removeProduct(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<ProductDto> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDto> products = transfer(repository.findAll());
        Page<ProductDto> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDto> productDtoList = transfer(repository.searchProductsList(keyword));
        Page<ProductDto> products = toPage(productDtoList, pageable);
        return products;
    }

    private Page toPage(List<ProductDto> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ProductDto> transfer(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();

            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setSellPrice(product.getSellPrice());
            productDto.setCategory(product.getCategory());
            productDto.setIs_deleted(product.getIs_deleted());
            productDto.setIs_activated(product.getIs_activated());

            productDtoList.add(productDto);
        }
        return productDtoList;
    }


    //    Customer
    @Override
    public List<Product> getAllProducts() {
        return repository.getAllProducts();
    }

    @Override
    public List<Product> listViewProduct() {
        return repository.listViewProduct();
    }

}
