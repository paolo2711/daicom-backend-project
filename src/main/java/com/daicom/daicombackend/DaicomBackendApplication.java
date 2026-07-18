package com.daicom.daicombackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; 

@SpringBootApplication
@EnableScheduling
public class DaicomBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaicomBackendApplication.class, args);
	}

}