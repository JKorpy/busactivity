package com.example.jkorpy.busactivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BusactivityApplication {


	public static void main(String[] args) {
		SpringApplication.run(BusactivityApplication.class, args);
	}

}
