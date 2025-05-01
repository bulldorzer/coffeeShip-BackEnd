package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.*;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import com.teamproject.coffeeShop.repository.CartCoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.CartRepository;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
/* 장바구니 서비스 구현 클래스 - 나영일 */
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final CartCoffeeBeanRepository cartCoffeeBeanRepository;
    private final CoffeeBeanRepository coffeeBeanRepository;

    // 장바구니 추가
    @Override
    public void addCart(String email, Long coffeeBeanId, int qty, boolean grindFlag) {
        // Step 1: email로 장바구니 조회 (없으면 새로 생성하여 cartId 반환)
        Long cartId = getCart(email); // getCart(email) 호출해서 cartId 얻기

        // Step 2: 커피 상품 존재 여부 확인
        CoffeeBean coffeeBean = coffeeBeanRepository.findById(coffeeBeanId)
                .orElseThrow(() -> new RuntimeException("CoffeeBean not found"));

        // Step 3: 장바구니에 이미 해당 상품이 있는지 확인
        Optional<CartCoffeeBean> existing = cartCoffeeBeanRepository.findByCartIdAndCoffeeBeanId(cartId, coffeeBeanId);

        // Step 4: 이미 존재하면 수량만 추가, 없으면 새로운 항목 추가
        if (existing.isPresent()) {
            CartCoffeeBean cartCoffeeBean = existing.get();
            cartCoffeeBean.changeQty(cartCoffeeBean.getQty() + qty);  // 기존 수량에 더함
            cartCoffeeBean.changeGrindFlag(grindFlag);  // 분쇄 여부 업데이트

            // 변경사항 저장
            cartCoffeeBeanRepository.save(cartCoffeeBean);
        } else {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            CartCoffeeBean cartCoffeeBean = CartCoffeeBean.builder()
                    .cart(cart)
                    .coffeeBean(coffeeBean)
                    .qty(qty)
                    .grindFlag(grindFlag)
                    .build();

            // 새 항목 추가
            cartCoffeeBeanRepository.save(cartCoffeeBean);
        }
    }


    // 회원의 이메일로 장바구니를 가져오기
    @Override
    public Long getCart(String email) {
        // 이메일로 cartId를 가져옴
        Optional<Long> cartIdOpt = cartRepository.getCartIdByEmail(email);

        // 장바구니가 없으면 생성 후 cartId 반환
        if (cartIdOpt.isEmpty()) {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Cart tempCart = Cart.builder().member(member).build();
            Cart savedCart = cartRepository.save(tempCart);
            return savedCart.getId();  // 새로 생성된 장바구니의 cartId 반환
        }

        // cartId 반환
        return cartIdOpt.get();
    }

    // 모든 장바구니 상품 목록 조회
    @Override
    public List<CartCoffeeBeanListDTO> getCartCoffeeBeans(String email) {
        List<Object[]> result = cartCoffeeBeanRepository.getCoffeeBeansOfCartDTOByEmail(email);
        log.info("이메일: {}", email);
        log.info("데이터: {}", result);
        log.info("데이터 길이: {}", result.size());

        List<CartCoffeeBeanListDTO> dtoList = new ArrayList<>();

        for (Object[] row : result) {
            CoffeeBean coffeeBean = coffeeBeanRepository.findById(((Number) row[3]).longValue()).orElse(null);

            String imageFile = null;
            if (coffeeBean != null && !coffeeBean.getImageList().isEmpty()) {
                imageFile = coffeeBean.getImageList().get(0).getFileName(); // 첫 이미지
            }

//            String memberShip = (String) row[8];
            String memberShip = "";
            if (row[8] instanceof MemberShip) {
                // MemberShip 객체에서 필요한 정보를 String으로 변환
                memberShip = ((MemberShip) row[8]).toString();  // 예: toString() 메서드 활용
            }


            CartCoffeeBeanListDTO dto = new CartCoffeeBeanListDTO(
                    ((Number) row[0]).longValue(), // cartCoffeeBeanId
                    ((Number) row[1]).longValue(), // cartId
                    ((Number) row[2]).longValue(), // memberId
                    ((Number) row[3]).longValue(), // coffeeBeanId
                    (String) row[4],               // name
                    ((Number) row[5]).intValue(),  // price
                    ((Number) row[6]).intValue(),  // qty
                    (Boolean) row[7],              // grindFlag
                    imageFile,                     // imageFile
                    memberShip                     // <-- 추가된 필드
            );

            dtoList.add(dto);
        }

        return dtoList;
    }


    // 특정 회원의 장바구니 특정 상품 삭제
    @Override
    public void removeOne(Long cartId, Long coffeeBeanId) {
        // 장바구니 원두가 존재하는지 확인
        Optional<CartCoffeeBean> cartCoffeeBean = cartCoffeeBeanRepository.findByCartIdAndCoffeeBeanId(cartId, coffeeBeanId);

        if (cartCoffeeBean.isPresent()) {
            // 원두가 장바구니에 존재하면 삭제
            cartCoffeeBeanRepository.deleteByCartIdAndCoffeeBeanId(cartId, coffeeBeanId);
        } else {
            // 원두가 장바구니에 없으면 예외를 던지거나 빈 목록 반환
            throw new RuntimeException("The coffee bean is not in the cart.");
        }
    }

    @Override
    public void removeAll(Long cartId) {
        cartCoffeeBeanRepository.deleteByCartId(cartId);
    }

    @Override
    @Transactional
    public void changeOption(CartCoffeeBeanListDTO cartCoffeeBeanDTO) {
        // cartId와 cartCoffeeBeanId를 통해 해당 상품을 찾아 업데이트
        CartCoffeeBean cartItem = cartCoffeeBeanRepository.findById(cartCoffeeBeanDTO.getCartCoffeeBeanId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // DTO에서 받은 값으로 업데이트
        cartItem.changeQty(cartCoffeeBeanDTO.getQty());
        cartItem.changeGrindFlag(cartCoffeeBeanDTO.isGrindFlag());

        // 업데이트된 정보 저장
        cartCoffeeBeanRepository.save(cartItem);
    }
}
