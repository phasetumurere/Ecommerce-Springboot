package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(name = "cost_price", nullable = false)
    private Double costPrice;
    @Column(nullable = false, name = "sell_price")
    private Double sellPrice;
    @Column(nullable = false, name = "current_quantity")
    private Integer currentQuantity;
    @Lob
    @Column(nullable = false, length = 16777215)
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
    private Boolean is_activated;
    private Boolean is_deleted;


}
