package com.teamproject.coffeeShop.repository;

// 관심상품Repository - 이재민
import com.teamproject.coffeeShop.domain.MemberSave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSaveRepository extends JpaRepository<MemberSave, Long> {
}
