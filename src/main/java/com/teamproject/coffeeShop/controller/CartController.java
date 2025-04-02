package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.service.CartService;
import com.teamproject.coffeeShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    // 현재 로그인된 사용자의 이메일과 파라미터로 전달된 이메일 주소가 같아야만 호출이 가능 -> 일치하지 않으면 Access Denied
//  @PreAuthorize("#cartCoffeeBeanDTO.email == authentication.name")
    @PostMapping("/change")
    // 수량 변경 요청 - 수량이 0보다 작게 들어오면 삭제, 1 이상으로 들어오면 변경
    public List<CartCoffeeBeanListDTO> changeCart(
            // 상품 추가 시 cartCoffeeBeanId 없이 JSON으로 전달함.
            // cartCoffeeBeanId 전달 시 이를 이용하므로 coffeeBeanId는 없어도 무방함.
            @RequestBody CartCoffeeBeanDTO cartCoffeeBeanDTO){

        log.info(cartCoffeeBeanDTO);
        if(cartCoffeeBeanDTO.getQty() <= 0) {
            return cartService.remove(cartCoffeeBeanDTO.getCartCoffeeBeanId());
        }
        return cartService.addOrModify(cartCoffeeBeanDTO);
    }


//  @PreAuthorize("hasAnyRole('ROLE_USER')")
//    @GetMapping("/coffeeBeans")
//    // 현재 로그인한 사용자를 기준으로 장바구니 원두 조회 -> 나중에 프론트 구현 이후 로그인하고 테스트, 사용
//    public List<CartCoffeeBeanListDTO> getCartCoffeeBeans(Principal principal) {
//
//        // java.security.Principal : 현재 사용자의 정보에 접근 가능
//        String email = principal.getName();
//        log.info("--------------------------------------------");
//        log.info("email: " + email );
//
//        return cartService.getCartCoffeeBeans(email);
//    }

    @GetMapping("/coffeeBeans/{memberId}")
    // 회원 고유번호를 기준으로 장바구니 원두 조회
    public List<CartCoffeeBeanListDTO> getCartCoffeeBeans(@PathVariable("memberId") Long memberId) {

        // memberId로 해당 MemberDTO 가져옴.
        MemberDTO memberDTO = memberService.getMember(memberId);

        // Member 객체에서 이메일을 추출
        String email = memberDTO.getEmail();

        return cartService.getCartCoffeeBeans(email);
    }

//  @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cartCoffeeBeanId}") // 장바구니에 담긴 원두 고유번호
    // 장바구니 원두 삭제
    public List<CartCoffeeBeanListDTO> removeFromCart( @PathVariable("cartCoffeeBeanId") Long cartCoffeeBeanId){

        log.info("CartCoffeeBean ID: " + cartCoffeeBeanId);
        return cartService.remove(cartCoffeeBeanId);
    }

}
