package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.CartCoffeeBean;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/* 장바구니, 원두 연결(조인) Repository : 나영일 */
public interface CartCoffeeBeanRepository extends JpaRepository<CartCoffeeBean, Long> {

    // 특정 사용자의 이메일을 통해서 해당 사용자의 모든 장바구니 원두를 조회
    // -> 로그인했을 때 사용자가 담은 모든 장바구니 원두를 조회 시 사용
    @Query("select "
            + "new com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO(ccb.id, ccb.qty, cb.id, cb.name, cb.price, "
            + "cb.country, cb.amount, cb.taste, cbi.fileName) "
            + "from CartCoffeeBean ccb "
            + "inner join Cart c on ccb.cart = c "
            + "left join CoffeeBean cb on ccb.coffeeBean = cb "
            + "left join cb.imageList cbi " // elementCollect 값타입 - on 생략 가능, 알아서 key를 찾아줌
            + "where c.member.email = :email "
            + "and cbi.ord = 0 "
            + "order by ccb.id desc")
    public List<CartCoffeeBeanListDTO> getCoffeeBeansOfCartDTOByEmail(@Param("email") String email);
    

    // 사용자의 이메일과 원두 고유번호로 해당 장바구니 원두를 알아내는 기능
    // -> 새로운 원두를 장바구니에 담고자 할 때 기존 장바구니 원두인지 확인하기 위해서 필요
    @Query("select ccb "
            + "from CartCoffeeBean ccb "
            + "inner join Cart c on ccb.cart = c "
            + "where c.member.email = :email "
            + "and ccb.coffeeBean.id = :id")
    public CartCoffeeBean getCoffeeBeanOfId(@Param("email") String email, @Param("id") Long id);

    // 장바구니 원두가 속한 장바구니의 고유번호를 알아내는 기능
    // -> 해당 장바구니 원두를 삭제한 후 해당 장바구니 원두가 속해 있는 장바구니의 모든 원두를 알아내기 위해서 필요
    @Query("select ccb.cart.id "
            + "from CartCoffeeBean ccb "
            + "where ccb.id = :id")
    public Long getCartFromCoffeeBean(@Param("id") Long id);

    // 특정한 장바구니의 고유번호만으로 해당 장바구닝의 모든 장바구니 원두들을 조회하는 기능
    // -> 특정한 장바구니 원두를 삭제한 후에 해당 장바구니 원두가 속해 있는 장바구니의 모든 장바구니 원두를 조회할 때 필요
    @Query("select "
            + "new com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO(ccb.id, ccb.qty, cb.id, cb.name, cb.price, "
            + "cb.country, cb.amount, cb.taste, cbi.fileName) "
            + "from CartCoffeeBean ccb "
            + "inner join Cart c on ccb.cart = c "
            + "left join CoffeeBean cb on ccb.coffeeBean = cb "
            + "left join cb.imageList cbi "
            + "where c.id = :id "
            + "and cbi.ord = 0 "
            + "order by ccb.id desc")
    public List<CartCoffeeBeanListDTO> getCoffeeBeansOfCartDTOByCart(@Param("id") Long id);
}

 /*
     JPQL - 엔티티 대상으로 조회를 실행
     SQL - db 테이블 대상으로 조회를 실행
     SELECT
         ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName
         FROM cart_item ci
         INNER JOIN cart mc ON ci.cart_cno = mc.cno
         LEFT JOIN product p ON ci.product_pno = p.pno
         LEFT JOIN product_image pi ON pi.product_pno = p.pno
         WHERE mc.owner_email = :email
         AND pi.ord = 0
         ORDER BY ci.cino DESC;
*/