package com.hm.oldiesbutgoodies.content.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentImage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Column(nullable = false)
    private String url;

    private int position;

}
