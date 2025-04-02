package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Cart;
import com.teamproject.coffeeShop.domain.CartCoffeeBean;
import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import com.teamproject.coffeeShop.repository.CartCoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.CartRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
/* 장바구니 서비스 구현 클래스 - 나영일 */
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final CartCoffeeBeanRepository cartCoffeeBeanRepository;

    @Override
    // 장바구니 원두 추가 또는 변경(수정)
    public List<CartCoffeeBeanListDTO> addOrModify(CartCoffeeBeanDTO cartCoffeeBeanDTO) {

        // DTO값 변수에 담아 놓기
        Long cartCoffeeBeanId = cartCoffeeBeanDTO.getCartCoffeeBeanId();    // 장바구니 원두 고유번호
        Long coffeeBeanId = cartCoffeeBeanDTO.getCoffeeBeanId();            // 원두 고유번호
        String email = cartCoffeeBeanDTO.getEmail();                        // 회원 이메일
        int qty = cartCoffeeBeanDTO.getQty();                               // 장바구니 원두 수량

        // 장바구니 원두 수량만 변경하는 경우
        if(cartCoffeeBeanId != null){

            // 수정할 상품 찾기
            Optional<CartCoffeeBean> cartCoffeeBeanResult =  cartCoffeeBeanRepository.findById(cartCoffeeBeanId);
            CartCoffeeBean cartCoffeeBean = cartCoffeeBeanResult.orElseThrow();

            cartCoffeeBean.changeQty(qty);
            cartCoffeeBeanRepository.save(cartCoffeeBean);

            return getCartCoffeeBeans(email); // 수정후 전체 장바구니 상품 목록 다시 불러오기
        }

        // 장바구니 가져오기 - 없으면 생성, 있으면 가져오기
        Cart cart = getCart(email);
        CartCoffeeBean cartCoffeeBean = null;

        cartCoffeeBean = cartCoffeeBeanRepository.getCoffeeBeanOfId(email, coffeeBeanId);
        if(cartCoffeeBean == null){
            CoffeeBean coffeeBean = CoffeeBean.builder().id(coffeeBeanId).build();
            cartCoffeeBean = CartCoffeeBean.builder().coffeeBean(coffeeBean).cart(cart).qty(qty).build();
        } else {
            cartCoffeeBean.changeQty(qty);
        }
        cartCoffeeBeanRepository.save(cartCoffeeBean);

        return getCartCoffeeBeans(email); // 현재 추가된 장바구니 원두 전체 목록 리턴
    }

    // 회원의 이메일로 장바구니를 가져오기
    private Cart getCart(String email) {
        Cart cart = null;
        Optional<Cart> result = cartRepository.getCartOfMember(email); // 이메일 기준으로 장바구니 찾아오기
        
        // 장바구니가 없으면 장바구니 생성
        if(result.isEmpty()){
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found")); // ✅ DB에서 조회 (직접 생성 시 에러 발생 가능)

            Cart tempCart = Cart.builder().member(member).build();
            cart = cartRepository.save(tempCart);
        } else{
            cart = result.get();
        }
        return cart;
    }

    @Override
    // 모든 장바구니 상품 목록 조회
    public List<CartCoffeeBeanListDTO> getCartCoffeeBeans(String email) {
        // 이메일 기준으로 cartCoffeeBean 전체 목록 조회
        return cartCoffeeBeanRepository.getCoffeeBeansOfCartDTOByEmail(email);
    }

    @Override
    // 장바구니 상품 삭제
    public List<CartCoffeeBeanListDTO> remove(Long cartCoffeeBeanId) {

        // 장바구니 원두로 장바구니 고유번호 가져오기
        Long cartId = cartCoffeeBeanRepository.getCartFromCoffeeBean(cartCoffeeBeanId);
        log.info("CartCoffeeBean ID: " + cartCoffeeBeanId);

        // 장바구니 안의 상품 삭제
        cartCoffeeBeanRepository.deleteById(cartCoffeeBeanId);

        // 장바구니 번호로 장바구니 상품 내역 다시 조회
        return cartCoffeeBeanRepository.getCoffeeBeansOfCartDTOByCart(cartId);
    }
}
