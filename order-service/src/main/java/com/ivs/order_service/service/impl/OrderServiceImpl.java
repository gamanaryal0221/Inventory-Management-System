package com.ivs.order_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ivs.order_service.domain.Order;
import com.ivs.order_service.domain.dto.OrderDTO;
import com.ivs.order_service.domain.dto.ProductDTO;
import com.ivs.order_service.domain.dto.UserDTO;
import com.ivs.order_service.repository.OrderRepository;
import com.ivs.order_service.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orderRepository.findAll()) {
            orderDTOs.add(convertToDTO(order));
        }
        return orderDTOs;
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
}