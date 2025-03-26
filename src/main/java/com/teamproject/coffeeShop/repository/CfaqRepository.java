package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Cfaq;
import com.teamproject.coffeeShop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/* 고객문의게시판 Repository - 진우 */
public interface CfaqRepository extends JpaRepository<Cfaq,Long> {
}
