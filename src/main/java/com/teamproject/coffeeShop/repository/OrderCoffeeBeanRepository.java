package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.domain.OrderCoffeeBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/* 주문서원두연결 Repository Repository - 진우 */
public interface OrderCoffeeBeanRepository extends JpaRepository<OrderCoffeeBean,Long> {
    // 특정 주문서의 모든 주문 아이템 조회
    List<OrderCoffeeBean> findByOrderId(Long orderId);

    // 특정 주문서의 모든 주문 아이템 삭제
    void deleteByOrder(Order order);
}
