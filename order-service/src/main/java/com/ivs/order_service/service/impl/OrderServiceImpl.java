package com.ivs.order_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ivs.order_service.domain.Order;
import com.ivs.order_service.domain.dto.OrderDTO;
import com.ivs.order_service.domain.dto.ProductDTO;
import com.ivs.order_service.domain.dto.UserDTO;
import com.ivs.order_service.repository.OrderRepository;
import com.ivs.order_service.service.OrderService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return convertToDTOList(orderList);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return convertToDTO(order.orElse(null));
    }

    @Override
    public OrderDTO createOrder(Order order) {
        return convertToDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO updateOrder(Long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            return convertToDTO(orderRepository.save(order));
        } else {
            return null;
        }

    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToDTO(Order order) {
        System.out.println("convertToDTO - Order:" + order);
        if (order == null) return null;
        UserDTO user = null;
        ProductDTO product = null;

        try {
            user = restTemplate.getForObject("http://user-service/users/" + order.getUserId(), UserDTO.class);
            System.out.println(order.getUserId() + " -> " + user);
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to fetch user data: " + order.getUserId() + " -> " + e.getMessage());
            user = new UserDTO();
            user.setId(order.getUserId());
        }

        try {
            product = restTemplate.getForObject("http://product-service/products/" + order.getProductId(), ProductDTO.class);
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to fetch product data: " + order.getProductId() + " -> " + e.getMessage());
            product = new ProductDTO();
            product.setId(order.getProductId());
        }

        return new OrderDTO(user, product, order.getId(), order.getQuantity(), order.getTotalPrice());
    }



    private List<OrderDTO> convertToDTOList(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) return Collections.emptyList();

        // Extracting unique userIds and productIds
        Set<String> userIdSet = orderList.stream().map(Order::getUserId).collect(Collectors.toSet());
        Set<String> productIdSet = orderList.stream().map(Order::getProductId).collect(Collectors.toSet());

        Map<String, UserDTO> userDTOMap = fetchUserDetails(userIdSet).stream().collect(Collectors.toMap(UserDTO::getId, user -> user));
        Map<String, ProductDTO> productDTOMap = fetchProductDetails(productIdSet).stream().collect(Collectors.toMap(ProductDTO::getId, product -> product));

        // Convert orders to OrderDTOs
        return orderList.stream()
                        .map(order -> {
                            UserDTO user = userDTOMap.getOrDefault(order.getUserId(), new UserDTO(order.getUserId()));
                            ProductDTO product = productDTOMap.getOrDefault(order.getProductId(), new ProductDTO(order.getProductId()));
                            return new OrderDTO(user, product, order.getId(), order.getQuantity(), order.getTotalPrice());
                        })
                        .collect(Collectors.toList());
    }

    private List<UserDTO> fetchUserDetails(Set<String> userIdSet) {
        try {
            return Arrays.asList(restTemplate.postForObject("http://user-service/users/batch", userIdSet, UserDTO[].class));
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to fetch user data: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    private List<ProductDTO> fetchProductDetails(Set<String> productIdSet) {
        try {
            return Arrays.asList(restTemplate.postForObject("http://product-service/products/batch", productIdSet, ProductDTO[].class));
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to fetch product data: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}