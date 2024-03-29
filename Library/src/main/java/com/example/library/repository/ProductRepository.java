package com.example.library.repository;

import com.example.library.dto.ProductDto;
import com.example.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select p from Product p where p.name like %?1% or p.description like  %?1%")
    Page<Product> searchProducts(String keyword, Pageable pageable);

    @Query("select p from Product p where p.name like %?1% or p.description like  %?1%")
    List<Product> searchProductsList(String keyword);

    //    Customer
    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false")
    List<Product> getAllProducts();

    @Query(value = "select * from products p where p.is_activated = true and p.is_deleted = false order by random() asc limit 4", nativeQuery = true)
    List<Product> listViewProduct();

    @Query("select p from Product p where p.category.id = ?1")
    List<Product> getRelatedCategory(Long categoryId);

    @Query("select p from Product p inner join Category c on c.id = ?1 and p.is_deleted = false and p.is_activated = true")
    List<Product> productsInCategory(Long categoryId);

    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false order by p.sellPrice desc")
    List<Product> filterHighPrices();

    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false order by p.sellPrice")
    List<Product> filterLowPrices();
}
