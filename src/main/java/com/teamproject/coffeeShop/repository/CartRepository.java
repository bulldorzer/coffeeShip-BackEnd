package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/* 장바구니 Repository - 나영일 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 이메일로 회원 장바구니Id 가져오는 기능
    @Query("SELECT c.id FROM Cart c WHERE c.member.email = :email")
    public Optional<Long> getCartIdByEmail(@Param("email") String email);
}
