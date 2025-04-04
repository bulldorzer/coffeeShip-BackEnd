package com.teamproject.coffeeShop.config;

import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.MemberRole;
import com.teamproject.coffeeShop.domain.MemberShip;
import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

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

    // Member -> MemberDTO 매핑 규칙 추가
    modelMapper.addMappings(new PropertyMap<Member, MemberDTO>() {
      @Override
      protected void configure() {
        // 기본 필드 매핑
        map(source.getEmail(), destination.getEmail());
        map(source.getName(), destination.getName());
        map(source.getPw(), destination.getPw());
        map(source.getPhone(), destination.getPhone());
        map(source.getPoint(), destination.getPoint());
        map(source.getCity(), destination.getCity());
        map(source.getStreet(), destination.getStreet());
        map(source.getZipcode(), destination.getZipcode());
        map(source.isSocial(), destination.isSocial());
        using(ctx -> ctx.getSource() == null ? null : ctx.getSource().toString())
                .map(source.getMemberShip(), destination.getMemberShip());

        // RoleList 매핑
        // MemberRole 리스트를 List<String>로 변환
        using(ctx -> ((List<MemberRole>) ctx.getSource()).stream()
                .map(Enum::name)
                .collect(Collectors.toList()))
                .map(source.getMemberRoleList(), destination.getRoleNames());
      }
    });

    return modelMapper;
  }
}
