package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import com.teamproject.coffeeShop.repository.DeliveryRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


// 배송Service 구현클래스 - 이재민
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryRepository deliveryRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    // 배송 생성
    @Override
    public Long createDelivery(DeliveryDTO deliveryDTO) {
        Optional<Member> result = memberRepository.findById(deliveryDTO.getMemberId());
        Member member = result.orElseThrow();
        Delivery delivery = Delivery.builder()
                .shipper(deliveryDTO.getShipper())
                .request(deliveryDTO.getRequest())
                .city(deliveryDTO.getCity())
                .street(deliveryDTO.getStreet())
                .zipcode(deliveryDTO.getZipcode())
                .status(deliveryDTO.getStatus())
                .member(member)
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

    // 특정 회원의 전체 배송 조회
    @Override
    public List<Delivery> getDeliveries(Long id) {
        List<Delivery> deliveries = deliveryRepository.findAllByMemberId(id);
        if(deliveries.isEmpty()) {
            throw new RuntimeException("배송 정보 없음");
        }

        return deliveries;
    }

    // 특정 회원의 특정 배송 조회
    @Override
    public Delivery getDeliveryById(Long memberId, Long deliveryId){
        Delivery delivery = deliveryRepository.findByMemberIdAndDeliveryId(memberId, deliveryId);
        if(delivery == null){
            throw new NoDataFoundException("해당 배송 없음");
        }
        return delivery;
    }


    // 배송 정보 변경
    @Transactional
    @Override
    public void updateDelivery(Long id, DeliveryDTO deliveryDTO) {
        Delivery existingDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Not Found"));
        existingDelivery.setShipper(deliveryDTO.getShipper());
        existingDelivery.setRequest(deliveryDTO.getRequest());
        existingDelivery.setCity(deliveryDTO.getCity());
        existingDelivery.setStreet(deliveryDTO.getStreet());
        existingDelivery.setZipcode(deliveryDTO.getZipcode());
        existingDelivery.setStatus(deliveryDTO.getStatus());

        deliveryRepository.save(existingDelivery);

    }

    // 배송 삭제
    @Transactional
    @Override
    public void deleteDelivery(Long id) {
        if(deliveryRepository.existsById(id)) {
            deliveryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Delivery Not Found");
        }
    }
}
