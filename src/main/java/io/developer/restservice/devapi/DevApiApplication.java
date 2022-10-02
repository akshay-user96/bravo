package io.developer.restservice.devapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevApiApplication {

	public static void main(String[] args) {
		System.out.println(" starting rest server ...");
		SpringApplication.run(DevApiApplication.class, args);
	}

}
