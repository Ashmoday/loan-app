package com.ashmoday.bets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetsApplication.class, args);
	}

}
