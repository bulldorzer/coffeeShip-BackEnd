package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.CartCoffeeBean;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/* 장바구니, 원두 연결(조인) Repository : 나영일 */
public interface CartCoffeeBeanRepository extends JpaRepository<CartCoffeeBean, Long> {

    // 이메일로 장바구니의 상품 가져오기
    @Query("select "
            + "new com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO(ccb.id, ccb.qty, cb.id, cb.name, cb.price, cbi.fileName) "
            + "from CartCoffeeBean ccb "
            + "inner join Cart c on ccb.cart = c "
            + "left join CoffeeBean cb on ccb.coffeeBean = cb "
            + "left join cb.imageList cbi " // elementCollect 값타입 - on 생략 가능, 알아서 key를 찾아줌
            + "where c.member.email = :email "
            + "and cbi.ord = 0 "
            + "order by ccb desc")
    public List<CartCoffeeBeanListDTO> getCoffeeBeansOfCartDTOByEmail(@Param("email") String email);
    

    // 특정 이메일의 장바구니에서 고유번호로 원두 가져오기
    @Query("select ccb "
            + "from CartCoffeeBean ccb "
            + "inner join Cart c on ccb.cart = c "
            + "where c.member.email = :email "
            + "and ccb.coffeeBean.id = :id")
    public CartCoffeeBean getCoffeeBeanOfId(@Param("email") String email, @Param("id") Long id);

    // 원두에서 장바구니 고유번호 가져오기
    @Query("select ccb.cart.id "
            + "from CartCoffeeBean ccb "
            + "where ccb.id = :id")
    public Long getCartFromCoffeeBean(@Param("id") Long id);

    // 장바구니 고유번호로 장바구니의 상품 가져오기
    @Query("select "
            + "new com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO(ccb.id, ccb.qty, cb.id, cb.name, cb.price, cbi.fileName) "
            + "from CartCoffeeBean ccb "
            + "inner join Cart c on ccb.cart = c "
            + "left join CoffeeBean cb on ccb.coffeeBean = cb "
            + "left join cb.imageList cbi "
            + "where c.id = :id "
            + "and cbi.ord = 0 "
            + "order by ccb desc")
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