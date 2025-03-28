package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import com.teamproject.coffeeShop.service.CartService;
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


    @PreAuthorize("#itemDTO.email == authentication.name")
    @PostMapping("/change")
    // 수량 변경 요청 - 수량이 0보다 작게 들어오면 삭제, 1 이상으로 들어오면 변경
    public List<CartCoffeeBeanListDTO> changeCart(@RequestBody CartCoffeeBeanDTO cartCoffeeBeanDTO){

        log.info(cartCoffeeBeanDTO);
        if(cartCoffeeBeanDTO.getQty() <= 0) {
            return cartService.remove(cartCoffeeBeanDTO.getCartCoffeeBeanId());
        }
        return cartService.addOrModify(cartCoffeeBeanDTO);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items")
    // 현재 로그인한 사용자를 기준으로 장바구니 원두 조회
    public List<CartCoffeeBeanListDTO> getCartCoffeeBeans(Principal principal) {

        String email = principal.getName();
        log.info("--------------------------------------------");
        log.info("email: " + email );

        return cartService.getCartCoffeeBeans(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cartCoffeeBeanId}") // 장바구니에 담긴 원두 고유번호
    // 장바구니 원두 삭제
    public List<CartCoffeeBeanListDTO> removeFromCart( @PathVariable("cartCoffeeBeanId") Long cartCoffeeBeanId){

        log.info("CartCoffeeBean ID: " + cartCoffeeBeanId);
        return cartService.remove(cartCoffeeBeanId);
    }

}
