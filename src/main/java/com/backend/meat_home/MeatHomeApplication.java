package com.backend.meat_home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeatHomeApplication {

	static {
		Dotenv.configure()
				.ignoreIfMalformed()
				.ignoreIfMissing()
				.load();
	}

	public static void main(String[] args) {
		SpringApplication.run(MeatHomeApplication.class, args);
	}

}
