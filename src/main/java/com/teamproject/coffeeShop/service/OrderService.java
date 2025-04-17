package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.OrderCoffeeBean;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.dto.OrderDTO;
import com.teamproject.coffeeShop.dto.OrderDetailsDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/* 주문서 서비스 - 진우 */
public interface OrderService {
    public Long createOrder(Long memberId, DeliveryDTO deliveryDTO);

    public OrderCoffeeBean addOrderCoffeeBean(Long orderId, Long coffeeBeanId, int qty);

    public List<OrderDTO> getAllOrders();

    public CustomPage<OrderDTO> getAllCoffeeBeansPaged(Pageable pageable, Long memberId);

    public void updateOrderComplete(OrderDTO orderDTO);

    public void cancelOrderCoffeeBean(Long orderCoffeeBeanId);

    public void cancelAllOrderCoffeeBeans(Long orderId);

    public List<OrderDetailsDTO> getOrderDetails(Long memberId);
}
