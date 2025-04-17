package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.dto.OrderDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/* 주문서 Repository - 진우 */
public interface OrderRepository extends JpaRepository<Order,Long> {

    // ✅ Fetch Join을 사용한 전체 조회 (Lazy Loading 문제 해결)
    @Query("SELECT o FROM Order o JOIN FETCH o.member m JOIN FETCH o.delivery d")
    List<Order> findAllByFetch();

    // ✅ 특정 날짜 이전 주문 검색
    List<Order> findByOrderDate(LocalDate beforeDate);

    // ✅ EntityGraph를 활용한 Lazy Loading 최적화 (N+1 문제 해결)
    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT o FROM Order o WHERE o.member.Id = :memberId")
    Page<Order> findAllByMemberId(@Param("memberId") Long memberId,Pageable pageable);

    // 마이페이지 주문내역 확인
    @Query("SELECT oc.order.id, oc.coffeeBean.id, oc.order.orderDate, oc.order.status, oc.name, oc.orderPrice, oc.qty " +
            "FROM OrderCoffeeBean oc " +
            "WHERE oc.order.member.id = :memberId")
    List<Object[]> findOrderDetailsByMember(@Param("memberId") Long memberId);

}
