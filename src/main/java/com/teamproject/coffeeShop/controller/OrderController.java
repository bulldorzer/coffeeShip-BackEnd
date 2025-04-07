package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.OrderCoffeeBean;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.dto.OrderDTO;
import com.teamproject.coffeeShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/{memberId}")
    public ResponseEntity<Long> createOrder(@PathVariable Long memberId, @RequestBody DeliveryDTO deliveryDTO) {
        return ResponseEntity.ok(orderService.createOrder(memberId, deliveryDTO));
    }

    // 전체 주문 조회 페이징 처리
    @GetMapping("/list/{memberId}")
    public ResponseEntity<CustomPage<OrderDTO>>
    getOrders(@PageableDefault(page = 0, size = 10) Pageable pageable,
              @PathVariable(name = "memberId") Long memberId) {
        return ResponseEntity.ok(orderService.getAllCoffeeBeansPaged(pageable, memberId));
    }

    // 주문서에 아이템 추가
    @PostMapping("/{orderId}/coffeeBean")
    public ResponseEntity<OrderCoffeeBean> addOrderItem(
            @PathVariable Long orderId,
            @RequestParam Long coffeeBeanId,
            @RequestParam int qty) {
        return ResponseEntity.ok(orderService.addOrderCoffeeBean(orderId, coffeeBeanId, qty));
    }

    // 특정 주문 아이템 취소
    @DeleteMapping("/coffeebean/{orderCoffeeBeanId}")
    public ResponseEntity<Map<String, Object>> removeOrderItem(@PathVariable Long orderCoffeeBeanId) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.cancelOrderCoffeeBean(orderCoffeeBeanId);
            response.put("message", "주문 아이템 삭제 성공");
            response.put("orderCoffeeBeanId", orderCoffeeBeanId);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(404).body(response);
        }
    }

//    // 전체 주문 취소
//    @DeleteMapping("/{orderId}/items")
//    public ResponseEntity<Void> cancelOrderItem(@PathVariable Long orderId) {
//        orderService.cancelAllOrderItems(orderId);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }
}
