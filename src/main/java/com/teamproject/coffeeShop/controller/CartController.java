package com.teamproject.coffeeShop.controller;

import com.sun.tools.jconsole.JConsoleContext;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.service.CartService;
import com.teamproject.coffeeShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 장바구니 원두 전체 조회
    @GetMapping("/items")
    public ResponseEntity<List<CartCoffeeBeanListDTO>> getCartCoffeeBeans(@RequestParam String email) {
        log.info("Received email: {}", email);
        List<CartCoffeeBeanListDTO> cartItems = cartService.getCartCoffeeBeans(email);
        return ResponseEntity.ok(cartItems);
    }

    // 장바구니 원두 수량과 분쇄 여부 변경
    @PutMapping("/changeOption")
    public ResponseEntity<Void> changeOption(@RequestBody CartCoffeeBeanListDTO cartCoffeeBeanDTO) {
        try {
            cartService.changeOption(cartCoffeeBeanDTO);
            return ResponseEntity.ok().build();  // 성공적으로 변경되면 200 OK 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // 오류가 발생하면 400 Bad Request 반환
        }
    }

    // 장바구니 특정 상품 삭제
    @DeleteMapping("/item")
    public ResponseEntity<Void> removeOne(@RequestParam Long cartId, @RequestParam Long coffeeBeanId) {
        cartService.removeOne(cartId, coffeeBeanId);
        return ResponseEntity.noContent().build(); // 삭제 완료 응답
    }

    // 장바구니 원두 전체 삭제
    @DeleteMapping("/items")
    public ResponseEntity<Void> removeAllItems(@RequestParam Long cartId) {
        cartService.removeAll(cartId);
        return ResponseEntity.noContent().build(); // 삭제 완료 응답
    }

    // 장바구니에 상품 추가
    @PostMapping("/item")
    public ResponseEntity<Void> addToCart(
            @RequestParam String email,  // 이메일을 받음
            @RequestBody CartCoffeeBeanDTO cartCoffeeBeanDTO) {

        // cartService.addCart 메서드 호출 시, 이메일을 전달하고, 서비스에서 장바구니 ID를 처리하도록 수정
        cartService.addCart(email, cartCoffeeBeanDTO.getCoffeeBeanId(), cartCoffeeBeanDTO.getQty(), cartCoffeeBeanDTO.isGrind_flag());

        // 201 Created 응답 반환
        return ResponseEntity.status(201).build();
    }

}
