package com.sparta.hanghaespringexpertlv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class HanghaeSpringExpertLv3Application {

    public static void main(String[] args) {
        SpringApplication.run(HanghaeSpringExpertLv3Application.class, args);
    }

}
