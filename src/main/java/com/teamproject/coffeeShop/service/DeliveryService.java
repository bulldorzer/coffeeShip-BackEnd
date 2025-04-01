package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import org.springframework.data.domain.Pageable;

// 배송Service - 이재민
public interface DeliveryService {
    Long createDelivery(DeliveryDTO deliveryDTO);
    CustomPage<DeliveryDTO> getAllDeliveriesPaged(Pageable pageable);
    Delivery getDelivery(Long id);
    void updateDelivery(Long id, DeliveryDTO deliveryDTO);
    void deleteDelivery(Long id);
}
