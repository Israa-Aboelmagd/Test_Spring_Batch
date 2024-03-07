package com.example.area_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AreaBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AreaBatchApplication.class, args);
	}

}
