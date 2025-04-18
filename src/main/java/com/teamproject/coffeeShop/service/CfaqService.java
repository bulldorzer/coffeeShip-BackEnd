package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.dto.CfaqDTO;
import com.teamproject.coffeeShop.dto.CustomPage;
import org.springframework.data.domain.Pageable;

// 고객 문의 게시판 - 진우 
public interface CfaqService {
    // 상품문의 글 등록
    public Long register(Long memberId,CfaqDTO cfaqDTO);

    // 상품문의글 상세보기
    public CfaqDTO getCfaq (Long cfaqId);

    // 전체 상품문의 목록 조회(페이징 포함)
    public CustomPage<CfaqDTO> getAllCfaqs(Pageable pageable);

    // 멤버가 작성한 상품 문의 후기 조회
    CustomPage<CfaqDTO> getCaqByMemberId(Long memberId, Pageable pageable);

    // 상품문의글 수정
    public void modify(CfaqDTO cfaqDTO);

    // 상품문의글 삭제
    public void remove(Long cfaqId);

    // 상품문의 답변상태 변경
    public void responseAnswer(Long cfaqId);

    
}
