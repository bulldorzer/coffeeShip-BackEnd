package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.CartCoffeeBean;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/* 장바구니, 원두 연결(조인) Repository : 나영일 */
public interface CartCoffeeBeanRepository extends JpaRepository<CartCoffeeBean, Long> {

    // 장바구니에 원두가 있는지 확인
    @Query("SELECT c FROM CartCoffeeBean c WHERE c.cart.id = :cartId AND c.coffeeBean.id = :coffeeBeanId")
    Optional<CartCoffeeBean> findByCartIdAndCoffeeBeanId(@Param("cartId") Long cartId, @Param("coffeeBeanId") Long coffeeBeanId);

    // 장바구니 원두 수량 변경
    @Modifying
    @Transactional
    @Query("UPDATE CartCoffeeBean c SET c.qty = :qty WHERE c.id = :cartCoffeeBeanId")
    void updateQty(@Param("cartCoffeeBeanId") Long cartCoffeeBeanId, @Param("qty") int qty);

    // 장바구니 원두 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM CartCoffeeBean c WHERE c.cart.id = :cartId AND c.coffeeBean.id = :coffeeBeanId")
    void deleteByCartIdAndCoffeeBeanId(@Param("cartId") Long cartId, @Param("coffeeBeanId") Long coffeeBeanId);


    // 특정 회원 장바구니 상품 전체 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM CartCoffeeBean c WHERE c.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    // 장바구니 조회 ( CartCoffeeBeanListDTO )
    @Query("SELECT c.id, c.cart.id, c.cart.member.id, c.coffeeBean.id, c.coffeeBean.name, c.coffeeBean.price, c.qty, c.grindFlag, member.memberShip " +
            "FROM CartCoffeeBean c " +
            "JOIN c.cart cart " +
            "JOIN c.coffeeBean coffeeBean " +
            "JOIN cart.member member " +
            "WHERE member.email = :email")
    List<Object[]> getCoffeeBeansOfCartDTOByEmail(@Param("email") String email);














}
