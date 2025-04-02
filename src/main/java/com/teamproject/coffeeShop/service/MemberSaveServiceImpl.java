package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.MemberSave;
import com.teamproject.coffeeShop.domain.MemberSaveItem;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.MemberSaveDTO;
import com.teamproject.coffeeShop.dto.MemberSaveListDTO;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import com.teamproject.coffeeShop.repository.MemberSaveItemRepository;
import com.teamproject.coffeeShop.repository.MemberSaveRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 관심상품Service 구현 클래스 - 이재민
@Service
@RequiredArgsConstructor
public class MemberSaveServiceImpl implements MemberSaveService{

    private final CoffeeBeanRepository coffeeBeanRepository;
    private final MemberSaveRepository memberSaveRepository;
    private final MemberRepository memberRepository;
    private final MemberSaveItemRepository memberSaveItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomPage<MemberSaveListDTO> getListMemberSave(Long id, Pageable pageable) {
        Page<MemberSaveItem> memberSavePage = memberSaveItemRepository.findItemsByMemberId(id, pageable);
        if(memberSavePage == null) {
            throw new NoDataFoundException("조회된 데이터 없음");
        }
        Page<MemberSaveListDTO> dtoPage = memberSavePage
                .map(memberSave -> modelMapper.map(memberSave, MemberSaveListDTO.class));
        int groupSize = 10;
        return CustomPage.of(dtoPage, groupSize);
    }

    @Override
    public Long addCoffeeBeans(MemberSaveDTO memberSaveDTO) {
        Long memberSaveId = memberSaveDTO.getMemberSaveId();
        Long memberId = memberSaveDTO.getMemberId();
        Long cfbId = memberSaveDTO.getCoffeeBeanId();

        MemberSave memberSave = getMemberSave(memberSaveDTO.getMemberId());
        MemberSaveItem memberSaveItem = null;
        boolean isAlreadySaved = memberSaveItemRepository.existsByMemberAndItem(memberId, cfbId);
        if(isAlreadySaved) {
            throw new RuntimeException("이미 관심상품 목록에 있습니다.");
        }
        CoffeeBean coffeeBean = CoffeeBean.builder().id(cfbId).build();
        memberSaveItem = MemberSaveItem.builder().coffeeBean(coffeeBean).memberSave(memberSave).build();

        memberSaveItemRepository.save(memberSaveItem);

        return memberSaveId;
    }

    @Override
    public void deleteCoffeeBean(Long memberId, Long coffeeBeanId) {
        memberSaveItemRepository.deleteByMemberSaveAndItemId(memberId, coffeeBeanId);
    }

    @Override
    public void deleteAllCoffeeBean(Long memberId) {
        memberSaveItemRepository.deleteAllByMemberSave(memberId);
    }

    private MemberSave getMemberSave(Long memberId) {
        MemberSave memberSave = null;
        Optional<MemberSave> result = memberSaveRepository.findByMemberId(memberId); // memberId로 관심상품 목록 찾기

        // 관심상품 목록이 없으면 새로운 MemberSave 생성
        if(result.isEmpty()){
            Member member = Member.builder().id(memberId).build();  // Member ID로 member 객체 생성
            MemberSave tempMemberSave = MemberSave.builder().member(member).build();
            memberSave = memberSaveRepository.save(tempMemberSave);
        } else {
            memberSave = result.get();
        }
        return memberSave;
    }

}
