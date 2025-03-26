package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {

}
