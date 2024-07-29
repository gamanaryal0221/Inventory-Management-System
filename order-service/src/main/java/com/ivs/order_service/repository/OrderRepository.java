package com.ivs.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ivs.order_service.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
