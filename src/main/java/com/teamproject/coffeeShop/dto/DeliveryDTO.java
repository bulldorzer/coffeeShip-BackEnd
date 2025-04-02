package com.teamproject.coffeeShop.dto;

// 배송DTO - 이재민
import com.teamproject.coffeeShop.domain.Delivery;
import com.teamproject.coffeeShop.domain.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long deliveryId;
    private String shipper;
    private String request;

    private String city;
    private String street;
    private String zipcode;
    private DeliveryStatus status;
    private Long memberId;

    public DeliveryDTO(Delivery delivery) {
        this.deliveryId = delivery.getId();
        this.shipper = delivery.getShipper();
        this.request = delivery.getRequest();
        this.city = delivery.getCity();
        this.street = delivery.getStreet();
        this.zipcode = delivery.getZipcode();
        this.status = delivery.getStatus();
        this.memberId = delivery.getMember().getId();
    }
}
