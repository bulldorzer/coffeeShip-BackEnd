package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeliveryService {
    void createDelivery(Delivery delivery);
    CustomPage<Delivery> getAllDeliveriesPaged(Pageable pageable);
    Delivery getDelivery(Long id);
    void updateDelivery(Long id, Delivery delivery);
    void deleteDelivery(Long id);
}
