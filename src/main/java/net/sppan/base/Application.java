package net.sppan.base;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableCaching
public class Application{

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	/**
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.debug("启动成功");
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
