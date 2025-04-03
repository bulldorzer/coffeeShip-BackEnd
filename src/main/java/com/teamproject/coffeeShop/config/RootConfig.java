package com.teamproject.coffeeShop.config;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

  @Bean
  public ModelMapper getMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setMatchingStrategy(MatchingStrategies.STANDARD); // LOOSE -> STANDARD 변경

    /*
        city, street, zipcode가 member와 delivery에 동시에 존재하고 있어
        어느 엔티티것을 매핑해야하는지에대한 오류가 발생함으로 수동으로 매핑해줌
        modelmapper 설정
    */
    // 🚀 Order -> OrderDTO 매핑 설정 추가
    modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
      mapper.map(src -> src.getDelivery().getCity(), OrderDTO::setCity);
      mapper.map(src -> src.getDelivery().getStreet(), OrderDTO::setStreet);
      mapper.map(src -> src.getDelivery().getZipcode(),OrderDTO::setZipcode);
    });

    return modelMapper;
  }
}
