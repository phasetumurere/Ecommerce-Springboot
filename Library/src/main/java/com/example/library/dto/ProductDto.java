package com.example.library.dto;

import com.example.library.model.Category;
import com.example.library.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double costPrice;
    private Double sellPrice;
    private Integer currentQuantity;
    private String image;
    private Category category;
    private Boolean is_activated;
    private Boolean is_deleted;
}
