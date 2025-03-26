package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/* 장바구니 Repository - 나영일 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 이메일로 회원의 장바구니 가져오기 (Member 가져온 뒤에 주석 해제)
//    @Query("select cart from Cart cart where cart.owner.email= :email")
//    public Optional<Cart> getCartOfMember(@Param("email") String email);
}
