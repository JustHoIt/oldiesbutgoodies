package com.hm.oldiesbutgoodies.domain;

import com.hm.oldiesbutgoodies.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Column(name = "url", nullable = false)
    private String url;

    private int position;


}
