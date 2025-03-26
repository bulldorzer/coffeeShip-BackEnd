package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.domain.Pfaq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PfaqRepository extends JpaRepository<Pfaq,Long> {
}
