package com.ashmoday.bets;

import com.ashmoday.bets.role.Role;
import com.ashmoday.bets.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetsApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository)
	{
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
		};
	}

}
