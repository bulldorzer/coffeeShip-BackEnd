package com.teamproject.coffeeShop.service;


import com.teamproject.coffeeShop.dto.CfaqDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class CfaqServiceTests {

    @Autowired
    CfaqService cfaqService;

    @Test
    public void 상품문의20개추가(){
        for (int i=0; i<20; i++){
            CfaqDTO cfaqDTO = CfaqDTO.builder()
                    .title("진우문의"+(2+i))
                    .writer("jinwoo"+(2+i))
                    .content("배송이 너무안와요.").build();

            cfaqService.register(cfaqDTO);
        }
    }
}
