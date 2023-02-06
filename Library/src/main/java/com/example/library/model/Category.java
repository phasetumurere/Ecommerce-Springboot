package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;
    @Column(name = "cat_name", unique = true, nullable = false)
    private String name;
    private Boolean is_activated;
    private Boolean is_deleted;

    public Category(String name) {
        this.name = name;
        this.is_activated = true;
        this.is_deleted = false;
    }
}
