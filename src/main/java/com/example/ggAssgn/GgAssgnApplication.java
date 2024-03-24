package com.example.ggAssgn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GgAssgnApplication {

	public static void main(String[] args) {
		SpringApplication.run(GgAssgnApplication.class, args);
	}

}
