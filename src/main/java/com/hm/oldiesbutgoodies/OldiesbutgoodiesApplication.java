package com.hm.oldiesbutgoodies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OldiesbutgoodiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(OldiesbutgoodiesApplication.class, args);
    }

}
