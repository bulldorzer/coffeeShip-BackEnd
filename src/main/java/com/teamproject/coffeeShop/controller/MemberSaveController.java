package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.MemberSaveDTO;
import com.teamproject.coffeeShop.dto.MemberSaveListDTO;
import com.teamproject.coffeeShop.service.MemberSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 관심상품Controller - 이재민
@RestController
@RequestMapping("/api/membersave")
@RequiredArgsConstructor
public class MemberSaveController {

    private final MemberSaveService memberSaveService;

    // 관심상품 전체 조회
    @GetMapping("/list/{id}")
    public ResponseEntity<CustomPage<MemberSaveListDTO>> getAllMemberSave(
            @PathVariable(name = "id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(memberSaveService.getListMemberSave(id, pageable));
    }

    // 관심상품 추가
    @PostMapping("/add")
    public ResponseEntity<MemberSaveDTO> addCoffeeBeans(@RequestBody MemberSaveDTO memberSaveDTO){
        Long memberSaveId = memberSaveService.addCoffeeBeans(memberSaveDTO);
        return ResponseEntity.ok(memberSaveDTO);
    }

    // 관심상품 삭제
    @DeleteMapping("/{memberId}/{cfbId}")
    public ResponseEntity<Void> deleteCoffeeBean(@PathVariable Long memberId, @PathVariable Long cfbId){
        memberSaveService.deleteCoffeeBean(memberId, cfbId);
        return ResponseEntity.noContent().build();
    }

    // 관심상품 전체 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllCoffeeBean(@PathVariable Long id) {
        memberSaveService.deleteAllCoffeeBean(id);
        return ResponseEntity.noContent().build();
    }
}
