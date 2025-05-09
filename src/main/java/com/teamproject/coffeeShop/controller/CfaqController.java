package com.teamproject.coffeeShop.controller;


import com.teamproject.coffeeShop.dto.CfaqDTO;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.service.CfaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cfaq")
public class CfaqController {

    private final CfaqService cfaqService;
    
    // 고객문의 글등록
    @PostMapping("/{memberId}")
    public ResponseEntity<Map<String,Long>> register(
            @PathVariable Long memberId,
            @RequestBody CfaqDTO cfaqDTO){

        try {
            log.info("MemberId: " + memberId+ " || CfaqDTO: "+cfaqDTO);

            Long cfaqId = cfaqService.register(memberId,cfaqDTO);

            return ResponseEntity.ok(Map.of("RESULT", cfaqId));
        } catch (Exception e) {
            throw new RuntimeException("ERROR");
        }


    }


    // 고객문의 상세보기
    @GetMapping("/list/{cfaqId}")
    public ResponseEntity<CfaqDTO> get(@PathVariable(name = "cfaqId") Long cfaqId){
        return ResponseEntity.ok(cfaqService.getCfaq(cfaqId));
    }

    // 특정 멤버가 작성한 고객문의 리스트 조회
    @GetMapping("/list/member/{memberId}")
    public ResponseEntity<CustomPage<CfaqDTO>> getCaqByMember(
            @PathVariable Long memberId,
            @PageableDefault(size = 6) Pageable pageable){

        return ResponseEntity.ok(cfaqService.getCaqByMemberId(memberId, pageable));
    }

    // 고객문의 전체목록보기 - 페이징처리
    @GetMapping("/list")
    public ResponseEntity<CustomPage<CfaqDTO>> list(@PageableDefault(page = 0, size = 10)Pageable pageable){
        return ResponseEntity.ok(cfaqService.getAllCfaqs(pageable));
    }

    // 고객문의 수정
    @PutMapping("/{cfaqId}")
    public ResponseEntity<Map<String,String>> modify(
            @PathVariable(name = "cfaqId") Long cfaqId,
            @RequestBody CfaqDTO cfaqDTO
    ){
        cfaqDTO.setId(cfaqId);

        log.info("Modify: "+cfaqDTO);
        cfaqService.modify(cfaqDTO);
        return ResponseEntity.ok(Map.of("RESULT","SUCCESS"));
    }

    // 고객문의 삭제
    @DeleteMapping("/{cfaqId}")
    public ResponseEntity<Map<String,String>> remove(
            @PathVariable(name = "cfaqId") Long cfaqId){
        log.info("Remove: "+cfaqId);
        cfaqService.remove(cfaqId);
        return ResponseEntity.ok(Map.of("RESULT","SUCCESS"));
    }


    // 고객문의 대답상태 변경
    @PutMapping("/response/{cfaqId}")
    public ResponseEntity<Map<String,String>> changeResponse(
            @PathVariable(name = "cfaqId") Long cfaqId
    ){
        log.info("Response: "+cfaqId);
        cfaqService.responseAnswer(cfaqId);
        return ResponseEntity.ok(Map.of("RESULT","SUCCESS"));
    }
}
