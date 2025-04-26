package com.hm.oldiesbutgoodies.content.domain;


import com.hm.oldiesbutgoodies.common.domain.BaseTimeEntity;
import com.hm.oldiesbutgoodies.content.dto.request.ProductRegisterDto;
import com.hm.oldiesbutgoodies.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    private int commentCount = 0;

    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "pickup_location", length = 100)
    private String pickupLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType;

    /** 이 상품에 대해 가격 조정 요청을 받을 수 있는지
     *  false = 불가능 , true = 가능 */
    @Column(name = "negotiable", nullable = false)
    private boolean negotiable = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public static Product from(ProductRegisterDto dto) {
        return Product.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .pickupLocation(dto.getPickupLocation())
                .deliveryType(dto.getDeliveryType())
                .negotiable(dto.isNegotiable())
                .status(dto.getStatus())
                .build();
    }

}
