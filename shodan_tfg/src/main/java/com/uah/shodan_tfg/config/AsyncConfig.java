package com.uah.shodan_tfg.config;

import java.util.concurrent.Executor;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	private static Logger LOGGER = Logger.getLogger(AsyncConfig.class);

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setQueueCapacity(10);
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setMaxPoolSize(4);
		taskExecutor.setThreadNamePrefix("ConnectionThread-");

		// Initialize taskExecutor
		taskExecutor.initialize();
		return taskExecutor;
	}
}
