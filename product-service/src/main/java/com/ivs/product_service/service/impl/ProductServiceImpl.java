package com.ivs.product_service.service.impl;

import com.ivs.product_service.domain.Product;
import com.ivs.product_service.domain.ProductDTO;
import com.ivs.product_service.domain.external.Inventory;
import com.ivs.product_service.repository.ProductRepository;
import com.ivs.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> productDtoList = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            productDtoList.add(convertToDTO(product));
        }
        return productDtoList;

    }

    @Override
    public ProductDTO getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        return convertToDTO(product.orElse(null));
    }

    @Override
    public ProductDTO createProduct(Product product) {
        return convertToDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(String id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            return convertToDTO(productRepository.save(product));
        } else {
            return null;
        }
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public void notifyInventoryService(HttpMethod httpMethod, String productId) {
        String inventoryServiceUrl = "http://inventory-service/inventory";
        if (httpMethod == HttpMethod.DELETE) inventoryServiceUrl += "/" + productId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Inventory> request = new HttpEntity<>(new Inventory(null, productId, null, null), headers);
        restTemplate.exchange(inventoryServiceUrl, httpMethod, request, String.class);
    }

    
    private ProductDTO convertToDTO(Product product) {
        if (product==null) return null;
        Inventory inventory = null;
        ProductDTO productDto = new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), null, null);

        try {
            inventory = restTemplate.getForObject("http://inventory-service/inventory/product/" + product.getId(), Inventory.class);
        } catch (Exception e) {
            System.err.println("Failed to fetch product's inventory detail: " + product.getId() + " -> " + e.getMessage());
        }

        if (inventory != null) {
            productDto.setTotalQuantity(inventory.getTotalQuantity());
            productDto.setReservedQuantity(inventory.getReservedQuantity());
        }

        return productDto;
    }
}
