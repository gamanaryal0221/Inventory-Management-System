package com.ivs.product_service.repository;

import com.ivs.product_service.domain.Product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
        List<Product> findAllById(Iterable<String> ids);
}
