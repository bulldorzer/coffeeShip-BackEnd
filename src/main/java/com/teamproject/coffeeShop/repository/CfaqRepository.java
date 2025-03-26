package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Cfaq;
import com.teamproject.coffeeShop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfaqRepository extends JpaRepository<Cfaq,Long> {
}
