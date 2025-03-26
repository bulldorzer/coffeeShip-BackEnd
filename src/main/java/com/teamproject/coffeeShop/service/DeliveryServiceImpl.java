package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryRepository deliveryRepository;

    @Override
    public void createDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    @Override
    public CustomPage<Delivery> getAllDeliveriesPaged(Pageable pageable){
        Page<Delivery> deliveryPage = deliveryRepository.findAll(pageable);
        if(deliveryPage == null) {
//            throw new NoDataFoundException("조회된 데이터 없음");
        }
        int groupSize = 10;
        return CustomPage.of(deliveryPage, groupSize);
    }

    @Override
    public Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Not Found"));
    }

    @Override
    public void updateDelivery(Long id, Delivery delivery) {

    }

    @Override
    public void deleteDelivery(Long id) {

    }
}
