package com.api.manager.fleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class FleetIoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleetIoApplication.class, args);
	}

}
