package com.ivs.product_service.service;

import com.ivs.product_service.domain.Product;
import com.ivs.product_service.domain.ProductDTO;

import java.util.List;

import org.springframework.http.HttpMethod;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(String id);
    ProductDTO createProduct(Product product);
    ProductDTO updateProduct(String id, Product product);
    void deleteProduct(String id);
    void notifyInventoryService(HttpMethod httpMethod, String productId);
    List<Product> getProductByIdInList(List<String> productIdList);
}
