package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.domain.Pfaq;
import org.springframework.data.jpa.repository.JpaRepository;

/* 상품문의 Repository - 진우 */
public interface PfaqRepository extends JpaRepository<Pfaq,Long> {
}
