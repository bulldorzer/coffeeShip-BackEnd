package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.repository.DeliveryRepository;
import com.teamproject.coffeeShop.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 배송Controller - 이재민
@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 배송 생성
    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        Long deliveryId = deliveryService.createDelivery(deliveryDTO);
        return ResponseEntity.ok(deliveryDTO);
    }

    // 배송 전체 조회
    @GetMapping("/list")
    public ResponseEntity<CustomPage<DeliveryDTO>> getAllDeliveries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(deliveryService.getAllDeliveriesPaged(pageable));
    }

    // 특정 배송 조회
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDelivery(id));
    }

    // 배송 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable Long id, @RequestBody Delivery delivery) {
        deliveryService.updateDelivery(id, delivery);
        return ResponseEntity.ok(deliveryService.getDelivery(id));
    }

    // 배송 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }


}
