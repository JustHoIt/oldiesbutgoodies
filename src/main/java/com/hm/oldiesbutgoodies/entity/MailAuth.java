package com.hm.oldiesbutgoodies.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.Duration;
import java.time.LocalDateTime;

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
