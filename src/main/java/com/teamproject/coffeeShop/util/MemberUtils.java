package com.teamproject.coffeeShop.util;

import com.teamproject.coffeeShop.dto.MemberDTO;

import java.util.HashMap;
import java.util.Map;

public class MemberUtils {

    public Map<String, Object> getClaims(MemberDTO memberDTO) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", memberDTO.getEmail());
        dataMap.put("pw", memberDTO.getPw());
        dataMap.put("name", memberDTO.getName());
        dataMap.put("social", memberDTO.isSocial());
        dataMap.put("roleNames", memberDTO.getRoleNames());

        return dataMap;
    }
}
