package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.dto.ReviewDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReviewServiceTests {

    @Autowired
    ReviewService reviewService;

    // 리뷰글 20개 등록
    @Test
    public void 리뷰글추가(){
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .score(4)
                .title("진우후기")
                .writer("jinwoo")
                .content("이런저런후기").build();

        Long result =reviewService.register(reviewDTO);

        log.info("등록된 리뷰 아이디 "+result);


    }

    @Test
    public void 리뷰글20개추가(){
        for (int i=0; i<20; i++ ){
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .score(4)
                    .title("진우후기"+6+i)
                    .writer("jinwoo"+6+i)
                    .content("이런저런후기").build();

            Long result =reviewService.register(reviewDTO);
        }
    }

}
