package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.OrderCoffeeBean;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.OrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/* 주문서 서비스 - 진우 */
public interface OrderService {
    public Long createOrder(Long memberId);
    public OrderCoffeeBean addOrderItem(Long orderId, Long itemId, int qty);
    public List<OrderDTO> getAllOrders();
    public CustomPage<OrderDTO> getAllItemsPaged(Pageable pageable);
    public void cancelOrderItem(Long orderItemId);
    public void cancelAllOrderItems(Long orderId);
}
