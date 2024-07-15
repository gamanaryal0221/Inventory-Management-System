package com.ivs.order_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ivs.order_service.domain.Order;
import com.ivs.order_service.domain.dto.OrderDTO;
import com.ivs.order_service.domain.dto.ProductDTO;
import com.ivs.order_service.domain.dto.UserDTO;
import com.ivs.order_service.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private List<Order> orderList = new ArrayList<>();
    private Long orderIdCounter = 1L;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orderList) {
            orderDTOs.add(convertToDTO(order));
        }
        return orderDTOs;
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderList.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();

        return order.map(this::convertToDTO).orElse(null);
    }

    @Override
    public OrderDTO createOrder(Order order) {
        order.setId(orderIdCounter++);
        orderList.add(order);
        return convertToDTO(order);
    }

    @Override
    public void updateOrder(Order order) {
        Optional<Order> existingOrder = orderList.stream()
                .filter(o -> o.getId().equals(order.getId()))
                .findFirst();
        existingOrder.ifPresent(o -> {
            o.setUserId(order.getUserId());
            o.setProductId(order.getProductId());
            o.setQuantity(order.getQuantity());
            o.setTotalPrice(order.getTotalPrice());
        });
    }

    @Override
    public void deleteOrder(Long id) {
        orderList.removeIf(o -> o.getId().equals(id));
    }

    private OrderDTO convertToDTO(Order order) {
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