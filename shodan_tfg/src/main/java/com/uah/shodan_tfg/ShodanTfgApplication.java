package com.uah.shodan_tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ShodanTfgApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShodanTfgApplication.class, args);
	}

}
