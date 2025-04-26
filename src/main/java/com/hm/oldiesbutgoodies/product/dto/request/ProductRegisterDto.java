package com.hm.oldiesbutgoodies.product.dto.request;

import com.hm.oldiesbutgoodies.product.domain.DeliveryType;
import com.hm.oldiesbutgoodies.product.domain.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductRegisterDto {

    private String title;

    private String description;

    private BigDecimal price;

    private String pickupLocation;

    private DeliveryType deliveryType;

    private boolean negotiable;

    private ProductStatus status;


}
