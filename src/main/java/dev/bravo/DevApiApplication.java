package dev.bravo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevApiApplication {

	public static void main(String[] args) {
		System.out.println("Starting rest server ...");
		SpringApplication.run(DevApiApplication.class, args);
	}

}
