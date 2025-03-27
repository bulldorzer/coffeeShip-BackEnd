package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.dto.CoffeeBeanDTO;
import com.teamproject.coffeeShop.service.CoffeeBeanService;
import com.teamproject.coffeeShop.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/api/coffeeBeans")
@RequiredArgsConstructor
/* 원두 컨트롤러 클래스 - 나영일 */
public class CoffeeBeanController {

    private final CustomFileUtil fileUtil;
    private final CoffeeBeanService coffeeBeanService;

    // 전체 상품 조회 (페이징 지원)
    @GetMapping("/list")
    public ResponseEntity<List<CoffeeBeanDTO>> getAllCoffeeBeans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(coffeeBeanService.getAllCoffeeBeans(pageable));
    }

    // 단일 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<CoffeeBeanDTO> getCoffeeBean(@PathVariable Long id) {
        return ResponseEntity.ok(coffeeBeanService.getCoffeeBean(id));
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        return fileUtil.getFile(fileName);
    }

    // 상품 등록 (파일 업로드 포함)
    @PostMapping("/add")
    public ResponseEntity<Long> createCoffeeBean(@ModelAttribute CoffeeBeanDTO coffeeBeanDTO) {
        // 파일 업로드
        List<MultipartFile> files = coffeeBeanDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        coffeeBeanDTO.setUploadFileNames(uploadFileNames);

        // 상품 저장 후 ID 반환
        Long savedCoffeeBeanId = coffeeBeanService.saveCoffeeBean(coffeeBeanDTO);
        return ResponseEntity.ok(savedCoffeeBeanId);
    }

    // 상품 삭제 (논리 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoffeeBean(@PathVariable Long id) {
        coffeeBeanService.deleteCoffeeBean(id);
        return ResponseEntity.noContent().build();
    }

    // 상품 수정 (파일 업로드 포함)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCoffeeBean(
            @PathVariable Long id,
            @ModelAttribute CoffeeBeanDTO coffeeBeanDTO
    ) {
        // 아이템 타입이
        // 📌 Postman에서 `files`가 정상적으로 넘어오는지 확인
        if (coffeeBeanDTO.getFiles() == null || coffeeBeanDTO.getFiles().isEmpty()) {
            System.out.println("⚠️ No files received!");
        } else {
            System.out.println("✅ Received files: " + coffeeBeanDTO.getFiles());
        }

        // 기존 아이템 정보 가져오기
        CoffeeBeanDTO oldCoffeeBeanDTO = coffeeBeanService.getCoffeeBean(id);

        // 기존 파일 목록 (DB에 저장된 파일들 - 삭제되었을 가능성 있음)
        List<String> oldFileNames = oldCoffeeBeanDTO.getUploadFileNames();
        System.out.println("Old file names: " + oldFileNames);

        // 새로 업로드된 파일 저장
        List<String> currentUploadFileNames = (coffeeBeanDTO.getFiles() != null && !coffeeBeanDTO.getFiles().isEmpty())
                ? fileUtil.saveFiles(coffeeBeanDTO.getFiles())
                : new ArrayList<>();
        System.out.println("Saved new files: " + currentUploadFileNames);

        // 화면에서 유지된 파일 목록
        List<String> uploadedFileNames = new ArrayList<>(oldFileNames);
        uploadedFileNames.addAll(currentUploadFileNames);
        coffeeBeanDTO.setUploadFileNames(uploadedFileNames);

        // 아이템 업데이트
        coffeeBeanService.updateCoffeeBean(id, coffeeBeanDTO);

        // 삭제해야 할 파일 찾기 (기존 파일 중에서 유지되지 않은 파일)
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> !uploadedFileNames.contains(fileName))
                    .collect(Collectors.toList());

            System.out.println("Files to be deleted: " + removeFiles);
            fileUtil.deleteFiles(removeFiles);
        }

        return ResponseEntity.ok(Map.of("RESULT", "SUCCESS"));
    }

    // 상품명으로 검색
    @GetMapping("/search")
    public ResponseEntity<List<CoffeeBeanDTO>> getCoffeeBeansByName(@RequestParam String name) {
        return ResponseEntity.ok(coffeeBeanService.getCoffeeBeansByName(name));
    }

    // ID 존재 여부 확인
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(coffeeBeanService.existsById(id));
    }
}
