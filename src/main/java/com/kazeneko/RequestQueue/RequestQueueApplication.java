package com.kazeneko.RequestQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RequestQueueApplication {
	public static void main(String[] args) {
		SpringApplication.run(RequestQueueApplication.class, args);
	}
}
