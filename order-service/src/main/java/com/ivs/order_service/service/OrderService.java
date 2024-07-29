package com.ivs.order_service.service;

import java.util.List;
import com.ivs.order_service.domain.Order;
import com.ivs.order_service.domain.dto.OrderDTO;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long id);
    OrderDTO createOrder(Order order);
    OrderDTO updateOrder(Long id, Order order);
    void deleteOrder(Long id);
}
