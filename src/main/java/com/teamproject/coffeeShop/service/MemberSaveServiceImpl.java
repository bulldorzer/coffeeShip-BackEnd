package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.domain.MemberSave;
import com.teamproject.coffeeShop.dto.MemberSaveDTO;
import com.teamproject.coffeeShop.dto.MemberSaveListDTO;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import com.teamproject.coffeeShop.repository.MemberSaveRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public List<MemberSaveListDTO> getListMemberSave(Long id) {
        return memberSaveRepository.getListOfMemberSaveById(id);
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
    public void removeCoffeeBean(Long memberId, Long coffeeBeanId) {
        memberSaveRepository.deleteByMemberIdAndCfbId(memberId, coffeeBeanId);
    }

    @Override
    public void removeAllCoffeeBean(Long memberId) {
        memberSaveRepository.deleteByMemberId(memberId);
    }

}
