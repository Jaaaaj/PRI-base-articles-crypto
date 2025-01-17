package fr.tse.fise3.pri.p002.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Gere l'execution et l'allocation des ressources d'un ensemble de threads.
 */
@Configuration
public class ThreadConfig {

	@Bean
	public TaskExecutor threadPoolTsskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(2);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

}
