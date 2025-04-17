package com.hm.oldiesbutgoodies.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Entity
@RedisHash("auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

}
