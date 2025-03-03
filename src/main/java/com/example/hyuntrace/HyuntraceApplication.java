package com.example.hyuntrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HyuntraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyuntraceApplication.class, args);
	}

}
