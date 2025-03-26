package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.CoffeeBeanImage;
import com.teamproject.coffeeShop.dto.CoffeeBeanDTO;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
/* 원두 서비스 구현 클래스 - 나영일 */
public class CoffeeBeanServiceImpl implements CoffeeBeanService{

    private final CoffeeBeanRepository coffeeBeanRepository;

    @Override
    // 전체 원두 수 반환
    public int getAllCoffeeBeansSize() {
        return coffeeBeanRepository.findAll().size();
    }

    @Override
    // 전체 원두 목록 조회 (페이징 포함)
    public List<CoffeeBeanDTO> getAllCoffeeBeans(Pageable pageable) {

        Page<Object[]> result = coffeeBeanRepository.selectList(pageable);

        return result.get().map(arr -> {
            CoffeeBean coffeeBean = (CoffeeBean) arr[0];
            CoffeeBeanImage coffeeBeanImage = (arr[1] != null) ? (CoffeeBeanImage) arr[1] : null;

            CoffeeBeanDTO coffeeBeanDTO = entityToDTO(coffeeBean);
            String imageFileName = (coffeeBeanImage != null) ? coffeeBeanImage.getFileName() : "default.png";
            coffeeBeanDTO.setUploadFileNames(List.of(imageFileName));

            return coffeeBeanDTO;
        }).collect(Collectors.toList());
    }

    @Override
    // 고유번호로 원두 가져오기
    public CoffeeBeanDTO getCoffeeBean(Long id) {
        CoffeeBean coffeeBean = coffeeBeanRepository.selectOne(id)
                .orElseThrow(() -> new RuntimeException("CoffeeBean Not Found"));
        return entityToDTO(coffeeBean);
    }

    @Transactional
    @Override
    // 받아온 원두를 저장
    public Long saveCoffeeBean(CoffeeBeanDTO coffeeBeanDTO) {
        CoffeeBean coffeeBean = dtoToEntity(coffeeBeanDTO);
        CoffeeBean savedCoffeeBean = coffeeBeanRepository.save(coffeeBean);
        return savedCoffeeBean.getId();
    }

    @Transactional
    @Override
    // 고유번호로 원두를 삭제
    public void deleteCoffeeBean(Long id) {
        if (coffeeBeanRepository.existsById(id)) {
            coffeeBeanRepository.updateToDelete(id, true);
        } else {
            throw new RuntimeException("Item not found");
        }
    }

    @Transactional
    @Override
    // 고유번호로 원두를 수정
    public void updateCoffeeBean(Long id, CoffeeBeanDTO coffeeBeanDTO) {

        CoffeeBean searchCoffeeBean = coffeeBeanRepository.findById(id).orElseThrow();

        if (coffeeBeanDTO.getName() != null) searchCoffeeBean.setName(coffeeBeanDTO.getName());
        if (coffeeBeanDTO.getPrice() != 0) searchCoffeeBean.setPrice(coffeeBeanDTO.getPrice());
        if (coffeeBeanDTO.getCountry() != null) searchCoffeeBean.setCountry(coffeeBeanDTO.getCountry());
        if (coffeeBeanDTO.getAmount() != null) searchCoffeeBean.setAmount(coffeeBeanDTO.getAmount());
        if (coffeeBeanDTO.getStockQuantity() != 0) searchCoffeeBean.setStockQuantity(coffeeBeanDTO.getStockQuantity());
        if (coffeeBeanDTO.getTaste() != null) searchCoffeeBean.setTaste(coffeeBeanDTO.getTaste());

        searchCoffeeBean.clearList();
        coffeeBeanRepository.save(searchCoffeeBean);

        List<String> uploadFileNames = coffeeBeanDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(searchCoffeeBean::addImageString);
        }

        coffeeBeanRepository.save(searchCoffeeBean);
    }

    @Override
    // 이름으로 원두 가져오기
    public List<CoffeeBeanDTO> getCoffeeBeansByName(String name) {
        return coffeeBeanRepository.findByName(name).stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    // 고유번호가 존재하는지 확인
    public boolean existsById(Long id) {
        return coffeeBeanRepository.existsById(id);
    }

    // 엔티티 -> DTO 변환 메서드
    private CoffeeBeanDTO entityToDTO(CoffeeBean coffeeBean) {

        CoffeeBeanDTO coffeeBeanDTO = CoffeeBeanDTO.builder()
                .id(coffeeBean.getId())
                .name(coffeeBean.getName())
                .price(coffeeBean.getPrice())
                .country(coffeeBean.getCountry())
                .amount(coffeeBean.getAmount())
                .stockQuantity(coffeeBean.getStockQuantity())
                .taste(coffeeBean.getTaste())
                .delFlag(coffeeBean.isDelFlag())
                .eventFlag(coffeeBean.isEventFlag())
                .grindFlag(coffeeBean.isGrindFlag())
                .build();

        List<String> fileNameList = coffeeBean.getImageList().stream()
                .map(CoffeeBeanImage::getFileName)
                .collect(Collectors.toList());

        coffeeBeanDTO.setUploadFileNames(fileNameList);

        return coffeeBeanDTO;
    }

    // DTO -> 엔티티 변환 메서드
    private CoffeeBean dtoToEntity(CoffeeBeanDTO coffeeBeanDTO) {

        CoffeeBean coffeeBean = CoffeeBean.builder()
                .name(coffeeBeanDTO.getName())
                .price(coffeeBeanDTO.getPrice())
                .country(coffeeBeanDTO.getCountry())
                .amount(coffeeBeanDTO.getAmount())
                .stockQuantity(coffeeBeanDTO.getStockQuantity())
                .taste(coffeeBeanDTO.getTaste())
                .delFlag(coffeeBeanDTO.isDelFlag())
                .eventFlag(coffeeBeanDTO.isEventFlag())
                .grindFlag(coffeeBeanDTO.isGrindFlag())
                .build();

        List<String> uploadFileNames = coffeeBeanDTO.getUploadFileNames();

        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(coffeeBean::addImageString);
        }

        return coffeeBean;
    }
}
