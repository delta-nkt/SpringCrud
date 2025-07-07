package com.example.DEMO_INTEGRATION;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoIntegrationApplication.class, args);
	}

}
