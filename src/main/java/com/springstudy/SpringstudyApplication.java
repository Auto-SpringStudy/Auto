package com.springstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync  // ✅ 비동기 실행 활성화
@EnableScheduling
public class SpringstudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringstudyApplication.class, args);
	}

}
