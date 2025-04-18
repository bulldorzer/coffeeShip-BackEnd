package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Answer;
import com.teamproject.coffeeShop.domain.Cfaq;
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.dto.CfaqDTO;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.repository.CfaqRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


/* 고객문의 게시판 구현 - 진우 */
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CfaqServiceImpl implements CfaqService{

    private final ModelMapper modelMapper;
    private final CfaqRepository cfaqRepository;
    private final MemberRepository memberRepository;

    // 고객문의 글등록
    @Override
    public Long register(Long memberId,CfaqDTO cfaqDTO) {

        log.info("Cfaq board register");

        // 멤버아이디를 받아 멤버엔티티 가져옴
        Member member  = memberRepository.findById(memberId).orElseThrow(
                ()->new IllegalArgumentException("해당 회원은 존재하지 않습니다.")
        );

        // postDate가 null이면 오늘 날짜로 설정
        if (cfaqDTO.getPostDate()==null){
            cfaqDTO.setPostDate(LocalDate.now());
        }

        // CfaqDTO를 cfaq엔티티로 변환
        Cfaq cfaq = modelMapper.map(cfaqDTO,Cfaq.class);

        // 대답상태가 null 일때 CHECK로 변환
        if (cfaq.getAnswer()==null){
            cfaq.changeAnswer(Answer.CHECK);
        }
        
        // Cfaq에 member 값설정
        cfaq.changeMember(member);
        
        // Cfaq 데이터 DB에 저장
        Cfaq saveCfaq = cfaqRepository.save(cfaq);

        // Cfaq 저장 아이디 반환
        return saveCfaq.getId();
    }

    // 고객문의 글 상세보기
    @Override
    public CfaqDTO getCfaq(Long cfaqId) {

        log.info("Cfaq board getPfaq");

        // 엔티티 자료 추출
        Optional<Cfaq> result = cfaqRepository.findById(cfaqId);

        // 자료형 변경 Optional -> Cfaq
        Cfaq cfaq = result.orElseThrow();

        // 자료형 변경 Cfaq -> CfaqDTO
        CfaqDTO cfaqDTO = modelMapper.map(cfaq,CfaqDTO.class);

        return cfaqDTO;
    }

    // 고객문의 전체 글보기 - 페이징 처리
    @Override
    public CustomPage<CfaqDTO> getAllCfaqs(Pageable pageable) {
        log.info("Cfaq board getAllPfaqs");

        // Cfaq데이터 페이징 처리된 데이터 조회
        Page<Cfaq> cfaqPage = cfaqRepository.findAll(pageable);

        // 조회된 페이지 없으면 예외처리
        if (cfaqPage.isEmpty()) throw new IllegalArgumentException("조회된 데이터가 존재하지 않습니다.");

        // 엔티티 -> DTO 변환
        Page<CfaqDTO> dtoPage = cfaqPage.map(cfaq -> modelMapper.map(cfaq,CfaqDTO.class));

        // DTO 페이지 네이션 정보 추가 별도의 DTO 만들기
        int groupSize = 10;
        return CustomPage.of(dtoPage,groupSize);
    }

    // 멤버가 작성한 상품 문의 후기 조회
    @Override
    public CustomPage<CfaqDTO> getCaqByMemberId(Long memberId, Pageable pageable) {
        Page<Cfaq> result = cfaqRepository.findByMemberId(memberId,pageable);

        if (result.isEmpty()){
            throw new IllegalArgumentException("조회된 데이터가 없습니다.");
        }

        Page<CfaqDTO> dtoPage = result.map( cfaq -> modelMapper.map(cfaq, CfaqDTO.class));
        int groupSize = 10;
        return CustomPage.of(dtoPage, groupSize);
    }

    // 고객문의 글 수정
    @Override
    public void modify(CfaqDTO cfaqDTO) {
        log.info("Cfaq board modify");
        
        // 수정할 고개문의 데이터 가져오기
        Optional<Cfaq> result = cfaqRepository.findById(cfaqDTO.getId());
        
        // 자료형 변경 Optional -> Cfaq
        Cfaq cfaq = result.orElseThrow();
        
        // 화면에서 가져온 데이터로 엔티티 데이터 변경
        cfaq.changeTitle(cfaqDTO.getTitle());
        cfaq.changeContent(cfaqDTO.getContent());
        cfaq.changePostDate(LocalDate.now());
        
        // 수정 데이터 저장
        cfaqRepository.save(cfaq);

    }

    // 고객문의 글 삭제
    @Override
    public void remove(Long cfaqId) {
        log.info("Cfaq board Remove");
        cfaqRepository.deleteById(cfaqId);
    }

    // 고객문의 대답상태 변경
    @Override
    public void responseAnswer(Long cfaqId) {

        // 수정할 고객문의 데이터 가져오기
        Optional<Cfaq> result =cfaqRepository.findById(cfaqId);

        // 자료형 변경 Optional -> Cfaq
        Cfaq cfaq = result.orElseThrow();

        // 대답상태가 체크일때 대답상태로 변경
        if (cfaq.getAnswer()==Answer.CHECK){
            cfaq.changeAnswer(Answer.RESPONSE);
        }else {
            throw new IllegalArgumentException("이미 응답완료된 문의입니다.");
        }

    }
}
