package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.*;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.OrderDTO;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import com.teamproject.coffeeShop.repository.OrderCoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.OrderRepository;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{


    private final OrderRepository orderRepository;
    private final OrderCoffeeBeanRepository orderCoffeeBeanRepository;
    private final CoffeeBeanRepository coffeeBeanRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    
    // 주문서 생성 (아이템 추가 X)
    @Override
    public Long createOrder(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        
        // 배달 주소,상태 초기화
        Delivery delivery = new Delivery();
        delivery.setCity(member.getCity());
        delivery.setStreet(member.getStreet());
        delivery.setZipcode(member.getZipcode());
        delivery.setStatus(DeliveryStatus.READY);
        
        // 주문서 상태 초기화
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.ORDER);
        
        // Orders테이블에 저장 후 아이디 반환
        orderRepository.save(order);
        return order.getId();
    }

    // 주문서에 상품 추가
    @Override
    public OrderCoffeeBean addOrderCoffeeBean(Long orderId, Long coffeeBeanId, int qty) {
        
        // 해당 주문서 검색
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("해당 주문서는 존재하지 않습니다."));
        
        // 추가할 상품 검색
        CoffeeBean coffeeBean = coffeeBeanRepository.findById(coffeeBeanId)
                .orElseThrow(()->new IllegalArgumentException("해당상품은 존재하지 않습니다."));

        // 수량 정책오류 처리
        if (qty<0) throw new IllegalArgumentException("수량은 1 이상이여야 합니다.");

        // 주문 상품 생성
        OrderCoffeeBean orderCoffeeBean =
                OrderCoffeeBean.createOrderItem(order, coffeeBean, coffeeBean.getPrice(),qty);

        // DB에 저장
        return orderCoffeeBeanRepository.save(orderCoffeeBean);
    }

    // 전체주문 조회
    @Override
    @Transactional(readOnly = true) // 읽기만 가능 수정,삭제 X
    public List<OrderDTO> getAllOrders() {

        // DB에서 order테이블의 모든정보를 가져오는데 해당 정보를 DTO클래스의 List로 관리 (데이터가 여러개일수 있으므로)
        return orderRepository.findAllByFetch()
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    // 전체 주문 상품 페이징처리
    @Override
    public CustomPage<OrderDTO> getAllCoffeeBeansPaged(Pageable pageable) {
        // order엔티티를 페이징 처리된 데이터 전체 조회
        Page<Order> orderPage = orderRepository.findAll(pageable);

        // 조회된 페이지가 없으면 예외처리
        if (orderPage.isEmpty()) throw new NoDataFoundException("조회된 데이터가 존재하지 않습니다.");

        // 엔티티 -> DTO 변환
        Page<OrderDTO> dtoPage = orderPage.map(order -> modelMapper.map(order, OrderDTO.class));
        log.info("======<OrderDtoPage>======");
        log.info(dtoPage.getContent());

        // DTO에 페이지 네이션 정보 추가( 별도의 DTO 만들기)
        int groupSize = 10;
        return CustomPage.of(dtoPage,groupSize);
    }

    // 특정 주문 아이템 삭제(취소)
    @Override
    public void cancelOrderCoffeeBean(Long orderCoffeeBeanId) {

        // 삭제할 상품등록 주문서 탐색
        OrderCoffeeBean orderCoffeeBean = orderCoffeeBeanRepository.findById(orderCoffeeBeanId)
                .orElseThrow(()->new IllegalArgumentException("해당 주문서 존재하지 않습니다."));


        orderCoffeeBean.cancel(); // 주문서 아이템취소 메소드 => 주문서에 등록된 아이템을 삭제하면서 상품수량테이블 수량을 올려줌
        orderCoffeeBean.setOrder(null); // 관계해제 -> 등록된 상품을 order등록에 제외시킴
        orderCoffeeBeanRepository.delete(orderCoffeeBean); // DB에 주문서에 등록된 아이템을 삭제

    }

    // 전체 주문 취소
    @Override
    public void cancelAllOrderCoffeeBeans(Long orderId) {

        // 주문 취소할 주문서 탐색
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("해당 주문서는 존재하지 않습니다."));

        // 상품등록주문서를 orderId로 탐색
        List<OrderCoffeeBean> orderCoffeeBeanList = orderCoffeeBeanRepository.findByOrderId(orderId);
        if (orderCoffeeBeanList.isEmpty()) throw new IllegalArgumentException("취소할 상품이 없습니다.");

        // 주문서 상태 CANCEL로 변경
        order.cancel();

        // 다중 상품등록 주문서의 상품취소,order와 연결해재, DB데이터 삭제
        for (OrderCoffeeBean orderCoffeeBean : orderCoffeeBeanList){
            orderCoffeeBean.cancel();
            orderCoffeeBean.setOrder(null);
            orderCoffeeBeanRepository.delete(orderCoffeeBean);
        }

    }
}
