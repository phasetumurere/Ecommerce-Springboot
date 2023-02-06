package com.example.library.service.impl;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public List<Category> findAll() {

        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return repository.save(categorySave);
    }


    @Override
    public Category findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = null;
        try {
            categoryUpdate = repository.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.setIs_activated(false);
            categoryUpdate.setIs_deleted(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return repository.save(categoryUpdate);
    }

    public void deleteById(Long id) {
        Category categoryDelete = repository.getById(id);
        categoryDelete.setIs_activated(false);
        categoryDelete.setIs_deleted(true);
        repository.save(categoryDelete);
    }

    @Override
    public void enabledById(Long id) {
        Category categoryUpdate = repository.getById(id);
        categoryUpdate.setIs_deleted(false);
        categoryUpdate.setIs_activated(true);
        repository.save(categoryUpdate);

    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Category> findAllByActivated() {
        return repository.findAllByActivated();
    }

    //    Customer
    @Override
    public List<CategoryDto> getCategoryAndProduct() {
        return repository.getCategoryProduct();
    }
}
