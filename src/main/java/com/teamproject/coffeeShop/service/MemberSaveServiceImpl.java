package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.domain.MemberSave;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.MemberSaveDTO;
import com.teamproject.coffeeShop.dto.MemberSaveListDTO;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import com.teamproject.coffeeShop.repository.MemberSaveRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
// 관심상품Service 구현 클래스 - 이재민
@Service
@RequiredArgsConstructor
public class MemberSaveServiceImpl implements MemberSaveService{

    private final CoffeeBeanRepository coffeeBeanRepository;
    private final MemberSaveRepository memberSaveRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomPage<MemberSaveListDTO> getListMemberSave(Pageable pageable) {
        Page<MemberSave> memberSavePage = memberSaveRepository.findAll(pageable);
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
        Long memberId = memberSaveDTO.getMemberId();
        Long cfbId = memberSaveDTO.getCfbId();
        boolean isAlreadySaved = memberSaveRepository.existsByMemberIdAndCfbId(memberId, cfbId);
        if(isAlreadySaved) {
            throw new RuntimeException("이미 관심상품 목록에 있습니다.");
        }

        MemberSave memberSave = modelMapper.map(memberSaveDTO, MemberSave.class);

        return memberSaveRepository.save(memberSave).getId();
    }

    @Override
    public void deleteCoffeeBean(Long memberId, Long coffeeBeanId) {
        memberSaveRepository.deleteByMemberIdAndCfbId(memberId, coffeeBeanId);
    }

    @Override
    public void deleteAllCoffeeBean(Long memberId) {
        memberSaveRepository.deleteByMemberId(memberId);
    }

}
