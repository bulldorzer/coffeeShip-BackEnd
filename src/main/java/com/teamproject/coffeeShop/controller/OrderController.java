package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.OrderCoffeeBean;
import com.teamproject.coffeeShop.dto.*;
import com.teamproject.coffeeShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
            @RequestParam int qty,
            @RequestParam int addpoint,
            @RequestParam int usepoint,
            @RequestBody DeliveryDTO deliveryDTO) {
        return ResponseEntity.ok(orderService.addOrderCoffeeBean(orderId, coffeeBeanId, qty, addpoint, usepoint, deliveryDTO));
    }

    // 주문서 아이템 배달완료
    @PutMapping("/{orderId}/complete")
    public ResponseEntity<Map<String, Object>> updateOrderComplete(
            @PathVariable Long orderId,
            @RequestBody OrderDTO orderDTO) {

        Map<String, Object> result = new HashMap<>();
        try {
            orderDTO.setOrderId(orderId);
            orderService.updateOrderComplete(orderDTO);
            result.put("message", "주문 상태 변경 성공");
            result.put("orderId", orderId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            result.put("message", e.getMessage());
            result.put("status", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
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

    // 마이페이지 주문목록 조회
    @GetMapping("/details/{memberId}")
    public List<OrderDetailsDTO> getOrderDetails(@PathVariable Long memberId) {
        return orderService.getOrderDetails(memberId);
    }

    // 전체 주문 취소
    @DeleteMapping("/{orderId}/items")
    public ResponseEntity<Void> cancelOrderItem(@PathVariable Long orderId) {
        orderService.cancelAllOrderCoffeeBeans(orderId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
