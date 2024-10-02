package com.learnSpringBoot.dreamShops.controllers;

import com.learnSpringBoot.dreamShops.model.Product;
import com.learnSpringBoot.dreamShops.request.AddProductRequest;
import com.learnSpringBoot.dreamShops.request.ProductUpdateRequest;
import com.learnSpringBoot.dreamShops.response.APIResponse;
import com.learnSpringBoot.dreamShops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @Autowired
    private IProductService productService;
    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(new APIResponse("Success", productList));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable Long productId) {
        try{
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new APIResponse("Found the Product!",product));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/product/add")
    public ResponseEntity<APIResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok(new APIResponse("Add Product Success!", newProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{Id}/update")
    public ResponseEntity<APIResponse> updateProduct(@RequestBody ProductUpdateRequest updateRequest, @PathVariable Long Id) {
        try {
            Product updateP = productService.updateProduct(updateRequest, Id);
            return ResponseEntity.ok(new APIResponse("Product is updated!", updateP));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable Long productId) {
        try{
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new APIResponse("Product is Deleted!", productId));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Product ID not found!", null));
        }
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<APIResponse> getProductByBrandAndName(@RequestParam String brandName,
                                                                @RequestParam String productName) {
        try {
            List<Product> productList = productService.getProductsByBrandAndName(brandName, productName);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Product Not Found", null));
            }
            return ResponseEntity.ok(new APIResponse("Products retrieved by brand and product name", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/category-and-brand")
    public ResponseEntity<APIResponse> getProductByCategoryAndBrand(@RequestParam String brandName, @RequestParam String categoryName) {
        try {
            List<Product> productList = productService.getProductByCategoryAndBrand(categoryName, brandName);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Product Not Found", null));
            }
            return ResponseEntity.ok(new APIResponse("Products retrieved by category and brand name", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{name}")
    public ResponseEntity<APIResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> productList = productService.getProductsByName(name);
            if(productList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Products Not found", null));
            }
            return ResponseEntity.ok(new APIResponse("Success!", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/by/brand-name")
    public ResponseEntity<APIResponse> getProductByBrand(@RequestParam String brandName) {
        try {
            List<Product> productList = productService.getProductsByBrand(brandName);
            if(productList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Products Not found", null));
            }
            return ResponseEntity.ok(new APIResponse("Success!", productList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<APIResponse> findProductsByCategory(@PathVariable String category) {
        try{
            List<Product> products = productService.getProductsByCategory(category);
            if(products.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("Products not Found!",null));

            return ResponseEntity.ok(new APIResponse("Success!", products));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/count/by-brand/by-name")
    public ResponseEntity<APIResponse> countProductsByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            var count = productService.countProductsByBrandAndName(brandName, productName);
            return ResponseEntity.ok(new APIResponse("Product Count calculated successfully!", count));
        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse(e.getMessage(), null));
        }
    }
}
