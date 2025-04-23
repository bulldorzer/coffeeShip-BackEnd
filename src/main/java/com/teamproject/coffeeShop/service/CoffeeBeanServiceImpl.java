package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Category;
import com.teamproject.coffeeShop.domain.CategoryCoffeeBean;
import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.CoffeeBeanImage;
import com.teamproject.coffeeShop.dto.CoffeeBeanDTO;
import com.teamproject.coffeeShop.repository.CategoryCoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.CategoryRepository;
import com.teamproject.coffeeShop.repository.CoffeeBeanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final CategoryRepository categoryRepository;
    private final CategoryCoffeeBeanRepository categoryCoffeeBeanRepository;
    private final ModelMapper modelMapper;

    @Override
    // 전체 원두 수 반환
    public int getAllCoffeeBeansSize() {
        return coffeeBeanRepository.findAll().size();
    }


    @Override
    // 전체 원두 목록 조회 (페이징 제외)
    public List<CoffeeBeanDTO> getAllCoffeeBeans() {
        List<Object[]> result = coffeeBeanRepository.selectAll();

        return result.stream().map(arr -> {
            CoffeeBean coffeeBean = (CoffeeBean) arr[0];
            CoffeeBeanImage coffeeBeanImage = (arr[1] != null) ? (CoffeeBeanImage) arr[1] : null;

            CoffeeBeanDTO coffeeBeanDTO = modelMapper.map(coffeeBean, CoffeeBeanDTO.class);

            // CoffeeBeanDTO의 CategoryId를 설정
            List<Category> categories = categoryCoffeeBeanRepository.findCategoriesByCoffeeBeanId(coffeeBean.getId());
            List<Long> categoryIds = categories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            coffeeBeanDTO.setCategoryIds(categoryIds);

            // CoffeeBeanDTO의 uploadFileNames를 설정
            String imageFileName = (coffeeBeanImage != null) ? coffeeBeanImage.getFileName() : "default.png";
            coffeeBeanDTO.setUploadFileNames(List.of(imageFileName));

            return coffeeBeanDTO;
        }).collect(Collectors.toList());
    }

    @Override
    // 전체 원두 목록 조회 (페이징 포함)
    public Page<CoffeeBeanDTO> getAllCoffeeBeansPaged(Pageable pageable) {
        Page<Object[]> result = coffeeBeanRepository.selectList(pageable);

        return result.map(arr -> {
            CoffeeBean coffeeBean = (CoffeeBean) arr[0];
            CoffeeBeanImage coffeeBeanImage = (arr[1] != null) ? (CoffeeBeanImage) arr[1] : null;

            CoffeeBeanDTO coffeeBeanDTO = modelMapper.map(coffeeBean, CoffeeBeanDTO.class);

            // CoffeeBeanDTO의 CategoryId를 설정
            List<Category> categories = categoryCoffeeBeanRepository.findCategoriesByCoffeeBeanId(coffeeBean.getId());
            List<Long> categoryIds = categories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            coffeeBeanDTO.setCategoryIds(categoryIds);

            // CoffeeBeanDTO의 uploadFileNames를 설정
            String imageFileName = (coffeeBeanImage != null) ? coffeeBeanImage.getFileName() : "default.png";
            coffeeBeanDTO.setUploadFileNames(List.of(imageFileName));

            return coffeeBeanDTO;
        });
    }

    @Override
    // 고유번호로 원두 가져오기
    public CoffeeBeanDTO getCoffeeBeanById(Long id) {
        CoffeeBean coffeeBean = coffeeBeanRepository.selectOne(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 원두입니다."));
        CoffeeBeanDTO coffeeBeanDTO = modelMapper.map(coffeeBean, CoffeeBeanDTO.class);

        // CoffeeBeanDTO의 CategoryId를 설정
        List<Category> categories = categoryCoffeeBeanRepository.findCategoriesByCoffeeBeanId(coffeeBean.getId());
        List<Long> categoryIds = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        coffeeBeanDTO.setCategoryIds(categoryIds);

        // CoffeeBeanDTO의 uploadFileNames를 설정
        List<CoffeeBeanImage> coffeeBeanImageList = coffeeBean.getImageList();
        List<String> imageFileNames = (coffeeBeanImageList != null && !coffeeBeanImageList.isEmpty())
                ? coffeeBeanImageList.stream()
                .map(CoffeeBeanImage::getFileName)
                .collect(Collectors.toList())
                : List.of("default.png");
        coffeeBeanDTO.setUploadFileNames(imageFileNames);
        return coffeeBeanDTO;
    }

    @Override
    // 이름으로 원두 가져오기
    public List<CoffeeBeanDTO> getCoffeeBeansByName(String name) {
        return coffeeBeanRepository.findByName(name).stream()
                .map(coffeeBean -> modelMapper.map(coffeeBean, CoffeeBeanDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    // 받아온 원두를 저장
    public Long saveCoffeeBean(CoffeeBeanDTO coffeeBeanDTO) {

        CoffeeBean coffeeBean = modelMapper.map(coffeeBeanDTO, CoffeeBean.class);
        CoffeeBean savedCoffeeBean = coffeeBeanRepository.save(coffeeBean);

        // 카테고리 연동
        List<Long> categoryIds = coffeeBeanDTO.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Long categoryId : categoryIds) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));
                CategoryCoffeeBean link = new CategoryCoffeeBean(category, savedCoffeeBean);
                categoryCoffeeBeanRepository.save(link);
            }
        }

        // 이미지 리스트 매핑
        List<String> uploadFileNames = coffeeBeanDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(coffeeBean::addImageString);
        }

        return savedCoffeeBean.getId();
    }

    @Transactional
    @Override
    // 고유번호로 원두를 삭제
    public void deleteCoffeeBean(Long id) {
        if (coffeeBeanRepository.existsById(id)) {
            categoryCoffeeBeanRepository.deleteByCoffeeBeanId(id);  // 원두 삭제 시 카테고리 연결도 함께 삭제
            coffeeBeanRepository.updateToDelete(id, true);     // Soft Delete
        } else {
            throw new RuntimeException("존재하지 않는 원두입니다.");
        }
    }

    @Transactional
    @Override
    // 고유번호로 원두를 수정
    public void updateCoffeeBean(Long id, CoffeeBeanDTO coffeeBeanDTO) {

        CoffeeBean searchCoffeeBean = coffeeBeanRepository.findById(id).orElseThrow(
                () -> new RuntimeException("존재하지 않는 원두입니다."));

        // 필드 수정
        if (coffeeBeanDTO.getName() != null) searchCoffeeBean.setName(coffeeBeanDTO.getName());
        if (coffeeBeanDTO.getPrice() != 0) searchCoffeeBean.setPrice(coffeeBeanDTO.getPrice());
        if (coffeeBeanDTO.getCountry() != null) searchCoffeeBean.setCountry(coffeeBeanDTO.getCountry());
        if (coffeeBeanDTO.getAmount() != null) searchCoffeeBean.setAmount(coffeeBeanDTO.getAmount());
        if (coffeeBeanDTO.getStockQty() != 0) searchCoffeeBean.setStockQty(coffeeBeanDTO.getStockQty());
        if (coffeeBeanDTO.getTaste() != null) searchCoffeeBean.setTaste(coffeeBeanDTO.getTaste());

        searchCoffeeBean.clearList();
        coffeeBeanRepository.save(searchCoffeeBean);

        // 이미지 갱신
        List<String> uploadFileNames = coffeeBeanDTO.getUploadFileNames();
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(searchCoffeeBean::addImageString);
        }

        // 기존 카테고리 연결 제거
        categoryCoffeeBeanRepository.deleteByCoffeeBeanId(id);

        // 새로운 카테고리 연결 추가
        List<Long> categoryIds = coffeeBeanDTO.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Long categoryId : categoryIds) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));
                CategoryCoffeeBean link = new CategoryCoffeeBean(category, searchCoffeeBean);
                categoryCoffeeBeanRepository.save(link);
            }
        }

    }

    @Override
    // 고유번호가 존재하는지 확인
    public boolean existsById(Long id) {
        return coffeeBeanRepository.existsById(id);
    }

}
