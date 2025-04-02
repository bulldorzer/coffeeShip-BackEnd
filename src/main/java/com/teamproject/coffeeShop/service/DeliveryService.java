package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

// 배송Service - 이재민
public interface DeliveryService {
    Long createDelivery(DeliveryDTO deliveryDTO);
    CustomPage<DeliveryDTO> getAllDeliveriesPaged(Pageable pageable);
    List<Delivery> getDeliveries(Long id);
    Delivery getDeliveryById(Long memberId, Long deliveryId);
    void updateDelivery(Long id, DeliveryDTO deliveryDTO);
    void deleteDelivery(Long id);
}
