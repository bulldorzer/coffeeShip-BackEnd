package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import com.teamproject.coffeeShop.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    // 배송 생성
    @Override
    public Long createDelivery(DeliveryDTO deliveryDTO) {
        Delivery delivery = Delivery.builder()
                .shipper(deliveryDTO.getShipper())
                .request(deliveryDTO.getRequest())
                .city(deliveryDTO.getCity())
                .street(deliveryDTO.getStreet())
                .zipcode(deliveryDTO.getZipcode())
                .status(deliveryDTO.getStatus())
                .build();
        deliveryRepository.save(delivery);
        return delivery.getId();
    }

    // 전체 배송 조회
    @Override
    public CustomPage<DeliveryDTO> getAllDeliveriesPaged(Pageable pageable){
        Page<Delivery> deliveryPage = deliveryRepository.findAll(pageable);
        if(deliveryPage == null) {
            throw new NoDataFoundException("조회된 데이터 없음");
        }
        Page<DeliveryDTO> dtoPage = deliveryPage.map(delivery -> modelMapper.map(delivery, DeliveryDTO.class));
        int groupSize = 10;
        return CustomPage.of(dtoPage, groupSize);
    }

    // 배송 조회
    @Override
    public Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Not Found"));
    }

    // 배송 정보 변경
    @Override
    public void updateDelivery(Long id, Delivery delivery) {
        Delivery existingDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Not Found"));
        existingDelivery.setStatus(delivery.getStatus());
        existingDelivery.setCity(delivery.getCity());
        existingDelivery.setStreet(delivery.getStreet());
        existingDelivery.setZipcode(delivery.getZipcode());
        existingDelivery.setStatus(delivery.getStatus());

        deliveryRepository.save(existingDelivery);

    }

    // 배송 삭제
    @Override
    public void deleteDelivery(Long id) {
        if(deliveryRepository.existsById(id)) {
            deliveryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Delivery Not Found");
        }
    }
}
