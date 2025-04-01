package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.dto.PfaqDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class PfaqServiceTests {

    @Autowired
    PfaqService pfaqService;

    @Test
    public void 상품문의20개추가(){
        for (int i=0; i<20; i++){
            PfaqDTO pfaqDTO = PfaqDTO.builder()
                    .title("원두상품문의"+(3+i))
                    .writer("jinwoo"+(3+i))
                    .content("원두상태가 이상해요.").build();

            pfaqService.register(pfaqDTO);
        }
    }
}
