//package com.teamproject.coffeeShop.service;
//
//import com.teamproject.coffeeShop.domain.Cart;
//import com.teamproject.coffeeShop.domain.CartCoffeeBean;
//import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
//import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
//import com.teamproject.coffeeShop.repository.CartCoffeeBeanRepository;
//import com.teamproject.coffeeShop.repository.CartRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Log4j2
//@Service
//@RequiredArgsConstructor
///* 장바구니 서비스 구현 클래스 - 나영일 */
//public class CartServiceImpl implements CartService{
//
//    private final CartRepository cartRepository;
//    private final CartCoffeeBeanRepository cartCoffeeBeanRepository;
//
//    @Override
//    public List<CartCoffeeBeanListDTO> addOrModify(CartCoffeeBeanDTO cartCoffeeBeanDTO) {
//
//        // DTO값 변수에 담아 놓기
//        Long id = cartCoffeeBeanDTO.getId();
//        Long cartId = cartCoffeeBeanDTO.getCartId();
//        Long coffeeBeanId = cartCoffeeBeanDTO.getCoffeeBeanId();
//        int qty = cartCoffeeBeanDTO.getQty();
//
//        // 장바구니 수량만 변경하는 경우
//        if(id != null){
//            // 수정할 상품 찾기
//            Optional<CartCoffeeBean> cartItemResult =  cartCoffeeBeanRepository.findById(id);
//            CartCoffeeBean cartCoffeeBean = cartItemResult.orElseThrow();
//
//            cartCoffeeBean.changeQty(qty);
//            cartCoffeeBeanRepository.save(cartCoffeeBean);
//
//            return getCartItems(email); // 수정후 전체 장바구니 상품 목록 다시 불러오기
//        }
//
//        // 장바구니 가져오기 - 없으면 생성, 있으면 가져오기
//        Cart cart = getCart(email);
//        CartCoffeeBean cartCoffeeBean = null;
//
//        cartItem = cartItemRepository.getItemOfPno(email, pno);
//        if(cartItem == null){
//            Product product = Product.builder().pno(pno).build();
//            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
//        } else {
//            cartItem.changeQty(qty);
//        }
//        cartItemRepository.save(cartItem);
//
//        return getCartItems(email); // 현재 추가된 장바구니 전체 상품 목록 리턴
//    }
//
//    @Override
//    public List<CartCoffeeBeanListDTO> getCartItems(String email) {
//        return List.of();
//    }
//
//    @Override
//    public List<CartCoffeeBeanListDTO> remove(Long id) {
//        return List.of();
//    }
//}
