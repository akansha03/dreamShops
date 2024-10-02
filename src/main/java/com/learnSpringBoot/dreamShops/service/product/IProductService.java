package com.learnSpringBoot.dreamShops.service.product;

import com.learnSpringBoot.dreamShops.model.Product;
import com.learnSpringBoot.dreamShops.request.AddProductRequest;
import com.learnSpringBoot.dreamShops.request.ProductUpdateRequest;

import java.util.List;
public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long Id);
    void deleteProductById(Long Id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brandName);
    List<Product> getProductByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}
