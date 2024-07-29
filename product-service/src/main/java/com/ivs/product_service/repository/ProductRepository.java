package com.ivs.product_service.repository;

import com.ivs.product_service.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
