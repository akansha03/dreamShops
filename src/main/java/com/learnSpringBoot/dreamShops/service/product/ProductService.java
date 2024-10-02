package com.learnSpringBoot.dreamShops.service.product;

import com.learnSpringBoot.dreamShops.exceptions.ProductNotFoundException;
import com.learnSpringBoot.dreamShops.model.Category;
import com.learnSpringBoot.dreamShops.model.Product;
import com.learnSpringBoot.dreamShops.repository.CategoryRepository;
import com.learnSpringBoot.dreamShops.repository.ProductRepository;
import com.learnSpringBoot.dreamShops.request.AddProductRequest;
import com.learnSpringBoot.dreamShops.request.ProductUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Product addProduct(AddProductRequest product) {
        // Check if the category is found in the database
        // If yes, set it as a new product category - if not then save it as a new category then set it as a new product category

        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        product.setCategory(category);
        return productRepository.save(createProduct(product, category));
    }
    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                request.getBrand(),
                category
        );
    }
    @Override
    public Product getProductById(Long Id) {
        return productRepository.findById(Id)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
    }
    @Override
    public void deleteProductById(Long Id) {
        productRepository.findById(Id)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ProductNotFoundException("Product Not found");});
    }
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
    }
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());

        existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }
    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrandName(brand);
    }
    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryAndBrand(category, brand);
    }
    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
