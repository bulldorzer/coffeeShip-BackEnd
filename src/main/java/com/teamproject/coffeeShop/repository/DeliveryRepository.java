package com.teamproject.coffeeShop.repository;

// 배송Repository - 이재민
import com.teamproject.coffeeShop.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
