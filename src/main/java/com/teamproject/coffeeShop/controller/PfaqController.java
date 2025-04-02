package com.teamproject.coffeeShop.controller;


import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.PfaqDTO;
import com.teamproject.coffeeShop.service.PfaqService;
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
@RequestMapping("/api/pfaq")
public class PfaqController {

    private final PfaqService pfaqService;

    // 상품문의 글 등록
    @PostMapping("/")
    public ResponseEntity<Map<String,Long>> register(@RequestBody PfaqDTO pfaqDTO){
        log.info("PfaqDTO: "+pfaqDTO);

        Long pfaqId = pfaqService.register(pfaqDTO);

        return ResponseEntity.ok(Map.of("RESULT",pfaqId));
    }

    // 상품문의 상세보기
    @GetMapping("/list/{pfaqId}")
    public ResponseEntity<PfaqDTO> get(@PathVariable(name = "pfaqId") Long pfaqId){
        return ResponseEntity.ok(pfaqService.getPfaq(pfaqId));
    }

    // 상품문의 전체 목록보기 - 페이징처리
    @GetMapping("/list")
    public ResponseEntity<CustomPage<PfaqDTO>> list(@PageableDefault(page = 0,size = 10)Pageable pageable){
        return ResponseEntity.ok(pfaqService.getAllPfaqs(pageable));
    }


    // 상품문의 수정
    @PutMapping("/{pfaqId}")
    public ResponseEntity<Map<String,String>> modify(
            @PathVariable(name = "pfaqId") Long pfaqId,
            @RequestBody PfaqDTO pfaqDTO){

        pfaqDTO.setId(pfaqId);

        log.info("Modify: " + pfaqDTO);

        pfaqService.modify(pfaqDTO);

        return ResponseEntity.ok(Map.of("RESULT","SUCCESS"));
    }


    // 상품문의 삭제
    @DeleteMapping("/{pfaqId}")
    public ResponseEntity<Map<String,String>> remove(
            @PathVariable(name = "pfaqId") Long pfaqId){

        log.info("Remove: "+ pfaqId);

        pfaqService.remove(pfaqId);

        return ResponseEntity.ok(Map.of("RESEULT","SUCCESS"));
        
    }


    // 상품문의 대답상태 변경
    @PutMapping("/response/{pfaqId}")
    public ResponseEntity<Map<String,String>> changeResponse(
            @PathVariable(name = "pfaqId") Long pfaqId){
        log.info("changeResponse: "+pfaqId);

        pfaqService.responseAnswer(pfaqId);

        return ResponseEntity.ok(Map.of("RESULT","SUCCESS"));
    }


}
