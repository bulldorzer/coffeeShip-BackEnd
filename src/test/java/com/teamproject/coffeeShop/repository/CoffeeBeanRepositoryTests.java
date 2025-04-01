package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.CoffeeBeanTaste;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Log4j2
@SpringBootTest
public class CoffeeBeanRepositoryTests {

    @Autowired CoffeeBeanRepository coffeeBeanRepository;

    private static final String[] COUNTRIES = {"Ethiopia", "Colombia", "Brazil", "Kenya"};
    private static final String[] AMOUNT = {"200g", "500g", "1kg"};

    @Test
    // 원두(상품) 등록 테스트
    // 상품 10개 등록, 상품 1개당 이미지 파일 2개 등록
    public void testInsert() {

        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            CoffeeBean coffeeBean = CoffeeBean.builder()
                    .name("CoffeeBean" + i)
                    .price(5000 + 100 * random.nextInt(10))
                    .country(COUNTRIES[random.nextInt(COUNTRIES.length)])
                    .amount(AMOUNT[random.nextInt(AMOUNT.length)])
                    .stockQty(random.nextInt(10, 50))
                    .build();

            switch (random.nextInt(4)) {
                case 0 : coffeeBean.setTaste(CoffeeBeanTaste.SWEET); break;
                case 1 : coffeeBean.setTaste(CoffeeBeanTaste.NUTTY); break;
                case 2 : coffeeBean.setTaste(CoffeeBeanTaste.ROASTY); break;
                case 3 : coffeeBean.setTaste(CoffeeBeanTaste.CRISP); break;
            }

            if (coffeeBean.getAmount().equals("500g")) {
                coffeeBean.setPrice((int)(coffeeBean.getPrice() * 2.5));
            } else if (coffeeBean.getAmount().equals("1kg")) {
                coffeeBean.setPrice((coffeeBean.getPrice() * 5));
            }

            switch (coffeeBean.getCountry()) {
                case "Brazil" : coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "BrazilPremiumCerrado");
                    coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "BrazilSingleBlend"); break;
                case "Colombia" : coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "Colombia_Medellin");
                    coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "Colombia_supremo_huila"); break;
                case "Ethiopia" : coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "EthiopiaNaturalYirgacheffeG4");
                    coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "EthiopiaYirgacheffeG2Tierra"); break;
                case "Kenya" : coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "KenyaAAKiambu");
                    coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "LightKenyaAAKiambu"); break;
            }

            coffeeBeanRepository.save(coffeeBean);

            log.info("------------------------");
        }
    }

    @Transactional
    @Test
    // 원두 조회 테스트
    // @ElementCollection은 lazy loading 방식으로 동작 -> 데이터베이스에 두 번 접근 (쿼리 두 번 실행) -> @Transactional 적용 필요
    public void testRead() {

        Long coffeeBeanId = 11L;

        Optional<CoffeeBean> result = coffeeBeanRepository.findById(coffeeBeanId);

        CoffeeBean coffeeBean = result.orElseThrow();

        log.info(coffeeBean);   // --- 1
        log.info(coffeeBean.getImageList());    // --- 2
    }

    @Test
    // 원두 조회 테스트 2
    // selectOne : @EntityGraph를 이용해서 해당 속성(imageList)을 조인 처리 -> 테이블들을 조인 처리해서 한 번에 로딩 (@Transactional 필요 X)
    public void testRead2() {

        Long coffeeBeanId = 11L;

        Optional<CoffeeBean> result = coffeeBeanRepository.selectOne(coffeeBeanId);

        CoffeeBean coffeeBean = result.orElseThrow();

        log.info(coffeeBean);
        log.info(coffeeBean.getImageList());
    }

    @Commit
    @Transactional
    @Test
    // 논리 삭제 (Soft Delete) - delFlag 값 기준으로 상품 삭제 여부 구문, delete 대신 update문으로 처리
    // delFlag = true -> 삭제로 처리(불러오지 X), DB에 남아있음.
    public void testDelete() {
        Long coffeeBeanId = 12L;

        coffeeBeanRepository.updateToDelete(coffeeBeanId, true);
    }

    @Test
    public void testUpdate() {

        Long coffeeBeanId = 13L;

        CoffeeBean coffeeBean = coffeeBeanRepository.selectOne(coffeeBeanId).get();

        coffeeBean.changeName("CoffeeBean11");
        coffeeBean.changePrice(6000);
        coffeeBean.changeCountry("Brazil");
        coffeeBean.changeAmount("200g");
        coffeeBean.changeStockQty(30);
        coffeeBean.changeTaste(CoffeeBeanTaste.CRISP);

        coffeeBean.clearList();
        coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "BrazilPremiumCerrado");
        coffeeBean.addImageString(UUID.randomUUID().toString() + "_" + "BrazilSingleBlend");

        coffeeBeanRepository.save(coffeeBean);
    }

    @Test
    public void testList() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<Object[]> result = coffeeBeanRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));

    }
}

