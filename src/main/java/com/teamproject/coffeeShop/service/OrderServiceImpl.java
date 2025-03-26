package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.OrderCoffeeBean;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.OrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class OrderServiceImpl implements OrderService{
    @Override
    public Long createOrder(Long memberId) {
        return 0L;
    }

    @Override
    public OrderCoffeeBean addOrderItem(Long orderId, Long itemId, int qty) {
        return null;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return List.of();
    }

    @Override
    public CustomPage<OrderDTO> getAllItemsPaged(Pageable pageable) {
        return null;
    }

    @Override
    public void cancelOrderItem(Long orderItemId) {

    }

    @Override
    public void cancelAllOrderItems(Long orderId) {

    }
}
