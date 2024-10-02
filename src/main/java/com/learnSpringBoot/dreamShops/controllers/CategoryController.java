package com.learnSpringBoot.dreamShops.controllers;

import com.learnSpringBoot.dreamShops.exceptions.AlreadyExistsException;
import com.learnSpringBoot.dreamShops.exceptions.ResourceNotFoundException;
import com.learnSpringBoot.dreamShops.model.Category;
import com.learnSpringBoot.dreamShops.response.APIResponse;
import com.learnSpringBoot.dreamShops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping("/category/all")
    public ResponseEntity<APIResponse> getAllCategories() {
        try {
            List<Category> categoryList = categoryService.getAllCategories();
            return ResponseEntity.ok(new APIResponse("Found!", categoryList));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    @PostMapping("/category/add")
    public ResponseEntity<APIResponse> addCategory(@RequestBody Category name) {
        try {
            Category theCategory = categoryService.addCategory(name);
            return ResponseEntity.ok(new APIResponse("Success!", theCategory));
        }
        catch (AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse> getCategoryByID(@PathVariable Long categoryId) {
        try {
            Category newCategory = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new APIResponse("Found!", newCategory));
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Category Not Found!", null));
        }
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<APIResponse> getCategoryByID(@PathVariable String name) {
        try {
            Category newCategory = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new APIResponse("Found!", newCategory));
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Category Not Found!", null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new APIResponse("Deleted Category!", null));
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Category Not Found!", null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category newCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new APIResponse("Category Updated!", newCategory));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }
}
