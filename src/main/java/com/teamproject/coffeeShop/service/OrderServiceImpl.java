package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.*;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.DeliveryDTO;
import com.teamproject.coffeeShop.dto.OrderDTO;
import com.teamproject.coffeeShop.dto.OrderDetailsDTO;
import com.teamproject.coffeeShop.repository.*;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final DeliveryRepository deliveryRepository;

    private final DeliveryService deliveryService;
    private final ModelMapper modelMapper;

    // 주문서 생성 (아이템 추가 X)
    @Override
    public Long createOrder(Long memberId, DeliveryDTO deliveryDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 배달 주소,상태 초기화
//        Delivery delivery = new Delivery();
//        delivery.setShipper(deliveryDTO.getShipper());
//        delivery.setRequest(deliveryDTO.getRequest());
//        delivery.setCity(deliveryDTO.getCity());
//        delivery.setStreet(deliveryDTO.getStreet());
//        delivery.setZipcode(deliveryDTO.getZipcode());
//        delivery.setStatus(DeliveryStatus.READY);
//        delivery.setMember(member);
//        deliveryRepository.save(delivery);
        Long deliveryId = deliveryService.createDelivery(deliveryDTO);
        Optional<Delivery> result = deliveryRepository.findById(deliveryId);
        Delivery delivery = result.orElseThrow();



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
    public OrderCoffeeBean addOrderCoffeeBean(Long orderId, Long coffeeBeanId, int qty, DeliveryDTO deliveryDTO) {

        try {
            // 해당 주문서 검색
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(()->new IllegalArgumentException("해당 주문서는 존재하지 않습니다."));

            // 추가할 상품 검색
            CoffeeBean coffeeBean = coffeeBeanRepository.findById(coffeeBeanId)
                    .orElseThrow(()->new IllegalArgumentException("해당상품은 존재하지 않습니다."));

            // 수량 정책오류 처리
            if (qty<=0) throw new IllegalArgumentException("수량은 1 이상이여야 합니다.");

            // 주문 상품 생성
            OrderCoffeeBean orderCoffeeBean =
                    OrderCoffeeBean.createOrderItem(order, coffeeBean, coffeeBean.getPrice(),qty);

            // 주문서에 마지막 배송정보 변경
            Long deliveryId = order.getDelivery().getId();
            System.out.println("deliveryId = " + deliveryId);
            deliveryService.updateDelivery(deliveryId,deliveryDTO);

            // DB에 저장
            return orderCoffeeBeanRepository.save(orderCoffeeBean);
        } catch (Exception e) {
            e.printStackTrace(); // 또는 log.error("저장 실패", e);
            throw e;
        }

    }

    // 전체주문서 조회 사용하지 않음(확장성을 위해 놔둠)
    @Override
    @Transactional(readOnly = true) // 읽기만 가능 수정,삭제 X
    public List<OrderDTO> getAllOrders() {

        // DB에서 order테이블의 모든정보를 가져오는데 해당 정보를 DTO클래스의 List로 관리 (데이터가 여러개일수 있으므로)
        return orderRepository.findAllByFetch()
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    // 전체 주문서 페이징처리
    @Override
    public CustomPage<OrderDTO> getAllCoffeeBeansPaged(Pageable pageable, Long memberId) {

        // order엔티티를 페이징 처리된 데이터 전체 조회
        Page<Order> orderPage = orderRepository.findAllByMemberId(memberId, pageable);

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

    // 특정 주문서 완료 처리
    @Override
    public void updateOrderComplete(OrderDTO orderDTO) {

        // 해당하는 주문서 찾기
        Order order = orderRepository.findById(orderDTO.getOrderId())
                .orElseThrow(()->new IllegalArgumentException("해당 주문서는 존재하지 않습니다."));

        // 주문서상태 완료처리
        order.setStatus(OrderStatus.COMPLETE);
        log.info("order: ",order
        );
        // 수정
        orderRepository.save(order);

        // 배달아이디 추출
        Long deliveryId = order.getDelivery().getId();
        // 주문서에 해당하는 배달상태 찾기
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(()->new IllegalArgumentException("해당 주문서는 존재하지 않습니다."));

        // 배달 완료처리
        delivery.setStatus(DeliveryStatus.COMPLETE);

        deliveryRepository.save(delivery);
    }

    // 특정 주문서 삭제(취소)
    @Override
    public void cancelOrderCoffeeBean(Long orderCoffeeBeanId) {

        // 삭제할 상품등록 주문서 탐색
        OrderCoffeeBean orderCoffeeBean = orderCoffeeBeanRepository.findById(orderCoffeeBeanId)
                .orElseThrow(()->new IllegalArgumentException("해당 주문서 존재하지 않습니다."));


        orderCoffeeBean.cancel(); // 주문서 아이템취소 메소드 => 주문서에 등록된 아이템을 삭제하면서 상품수량테이블 수량을 올려줌
        orderCoffeeBean.setOrder(null); // 관계해제 -> 등록된 상품을 order등록에 제외시킴
        orderCoffeeBeanRepository.delete(orderCoffeeBean); // DB에 주문서에 등록된 아이템을 삭제

    }

    // 전체 주문서 취소
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

    // 마이페이지 주문목록 조회
    @Override
    public List<OrderDetailsDTO> getOrderDetails(Long memberId) {
        List<Object[]> results = orderRepository.findOrderDetailsByMember(memberId);
        List<OrderDetailsDTO> orderDetailsList = new ArrayList<>();

        for (Object[] result : results) {
            // Object[]에서 각각의 값을 추출
            Long orderId = (Long) result[0];  // 주문 아이디
            Long orderCoffeeBeanId = (Long) result[1];
            LocalDate orderDate = (LocalDate) result[2];
            String status = result[3].toString();
            String coffeeName = (String) result[4];
            int orderPrice = (int) result[5];
            int qty = (int) result[6];
            int totalPrice = orderPrice * qty;

            // DTO 객체로 변환
            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(orderId, orderCoffeeBeanId, orderDate, status, coffeeName, totalPrice, qty);
            orderDetailsList.add(orderDetailsDTO);
        }

        return orderDetailsList;
    }
}
