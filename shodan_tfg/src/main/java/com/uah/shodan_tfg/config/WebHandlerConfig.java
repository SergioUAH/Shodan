package com.uah.shodan_tfg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebHandlerConfig implements WebMvcConfigurer {

	private final long MAX_AGE = 4000;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE")
				.maxAge(MAX_AGE);
	}

}
