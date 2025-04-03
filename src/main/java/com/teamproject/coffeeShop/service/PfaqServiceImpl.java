package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Answer;
import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.Pfaq;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.PfaqDTO;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.MemberRepository;
import com.teamproject.coffeeShop.repository.PfaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/* 상품문의 게시판 구현 - 진우 */
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class PfaqServiceImpl implements PfaqService{

    private final ModelMapper modelMapper;
    private final PfaqRepository pfaqRepository;
    private final MemberRepository memberRepository;
    private final CoffeeBeanRepository coffeeBeanRepository;

    // 상품문의 글 등록
    @Override
    public Long register(Long memberId, Long coffeeBeanId, PfaqDTO pfaqDTO) {
        log.info("Pfaq board register");

        // 멤버아이디, 아이템아이디를 받아 멤버,아이템 엔티티 가져옴
        Member member  = memberRepository.findById(memberId).orElseThrow(
                ()->new IllegalArgumentException("해당 회원은 존재하지 않습니다.")
        );


        CoffeeBean coffeeBean = coffeeBeanRepository.findById(coffeeBeanId).orElseThrow(
                ()->new IllegalArgumentException("해당 상품은 존재하지 않습니다")
        );

        // PostDate가 null이면 오늘 날짜로 설정
        if (pfaqDTO.getPostDate() == null){
            pfaqDTO.setPostDate(LocalDate.now());
        }

        // PfaqDTO를 pfaq로 변환
        Pfaq pfaq = modelMapper.map(pfaqDTO,Pfaq.class);

        // Review에 member,coffeeBean 값설정
        pfaq.changeMember(member);
        pfaq.changeCoffeeBean(coffeeBean);

        // 대답상태가 null 일때 CHECK로 변환
        if (pfaq.getAnswer()==null){
            pfaq.changeAnswer(Answer.CHECK);
        }

        // Pfaq 데이터 DB에 저장
        Pfaq savePfaq = pfaqRepository.save(pfaq);

        // pfaq 저장 아이디 반환
        return savePfaq.getId();
    }

    // 상품문의 글 상세보기
    @Override
    public PfaqDTO getPfaq(Long pfaqId) {
        log.info("Pfaq board getPfaq");

        // 엔티티 자료 추출
        Optional<Pfaq> result = pfaqRepository.findById(pfaqId);

        // 자료형 변경 Optional -> Pfaq
        Pfaq pfaq = result.orElseThrow();

        // 자료형 변경 Pfaq -> PfaqDTO
        PfaqDTO dto = modelMapper.map(pfaq,PfaqDTO.class);

        return dto;
    }

    // 상품문의 글 전체보기 - 페이징처리
    @Override
    public CustomPage<PfaqDTO> getAllPfaqs(Pageable pageable) {
        log.info("Pfaq board getAllPfaqs");

        // Pfaq데이터 페이징 처리된 데이터 조회
        Page<Pfaq> pfaqPage = pfaqRepository.findAll(pageable);

        // 조회된 페이지 없으면 예외처리
        if (pfaqPage.isEmpty()) throw new IllegalArgumentException("조회된 데이터가 존재하지 않습니다.");

        // 엔티티 -> DTO 변환
        Page<PfaqDTO> dtoPage = pfaqPage.map(pfaq -> modelMapper.map(pfaq,PfaqDTO.class));
        log.info(dtoPage.getContent());

        // DTO 페이지 네이션 정보 추가 별도의 DTO 만들기
        int groupSize = 10;
        return CustomPage.of(dtoPage,groupSize);
    }

    // 상품문의 글 수정
    @Override
    public void modify(PfaqDTO pfaqDTO) {
        log.info("Pfaq board Modify");

        // 수정할 리뷰 데이터 가져오기
        Optional<Pfaq> result = pfaqRepository.findById(pfaqDTO.getId());

        // 자료형 변경 Optional -> Pfqa
        Pfaq pfaq = result.orElseThrow();

        // 화면에서 가져온 데이터로 수정
        pfaq.changeTitle(pfaqDTO.getTitle());
        pfaq.changeContent(pfaqDTO.getContent());
        pfaq.changePostDate(LocalDate.now());

        // 수정데이터 저장
        pfaqRepository.save(pfaq);

    }

    // 상품문의 글 삭제
    @Override
    public void remove(Long pfaqId) {
        log.info("Pfaq board Remove");
        pfaqRepository.deleteById(pfaqId);
    }

    // 상품문의 대답 상태 변경
    @Override
    public void responseAnswer(Long pfaqId) {

        // 수정할 리뷰 데이터 가져오기
        Optional<Pfaq> result = pfaqRepository.findById(pfaqId);

        // 자료형 변경 Optional -> Pfqa
        Pfaq pfaq = result.orElseThrow();

        // 대답상태가 체크일때 응답상태로 변경 만약 대답상태가 응답일때에는 예외처리
        if (pfaq.getAnswer() == Answer.CHECK){
            pfaq.changeAnswer(Answer.RESPONSE);
        }else {
            throw new IllegalArgumentException("이미 응답완료된 문의입니다.");
        }

        pfaqRepository.save(pfaq);
    }
}
