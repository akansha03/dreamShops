package com.learnSpringBoot.dreamShops.service.category;

import com.learnSpringBoot.dreamShops.exceptions.AlreadyExistsException;
import com.learnSpringBoot.dreamShops.exceptions.ResourceNotFoundException;
import com.learnSpringBoot.dreamShops.model.Category;
import com.learnSpringBoot.dreamShops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
    }
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {

        return Optional.of(category).filter(c ->
                !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(() ->
                        new AlreadyExistsException(category.getName() + "already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long Id) {
        return Optional.ofNullable(getCategoryById(Id)).map(
                oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository :: delete, ()-> {
                    throw new ResourceNotFoundException("Category not found");
                });
    }
}
