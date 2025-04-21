package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.PfaqDTO;
import org.springframework.data.domain.Pageable;

// 상품 문의 게시판 - 진우
public interface PfaqService {

    // 상품문의 글 등록
    public Long register(Long memberId, Long coffeeBeanId, PfaqDTO pfaqDTO);

    // 상품문의글 상세보기
    public PfaqDTO getPfaq (Long pfaqId);

    // 전체 상품문의 목록 조회(페이징 포함)
    public CustomPage<PfaqDTO> getAllPfaqs(Pageable pageable);

    // 특정 상품에 대한 상품문의 보기
    CustomPage<PfaqDTO> getPfaqByCoffeeBeanId(Long coffeeBeanId, Pageable pageable);

    // 특정 멤버가 작성한 상품문의 목록보기
    CustomPage<PfaqDTO> getPfaqByMemberId(Long memberId, Pageable pageable);

    // 상품문의글 수정
    public void modify(PfaqDTO pfaqDTO);

    // 상품문의글 삭제
    public void remove(Long pfaqId);

    // 상품문의 답변상태 변경
    public void responseAnswer(Long pfaqId);


}
