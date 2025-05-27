package com.krishna.devdrills;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
public class DevDrillsApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DevDrillsApplication.class, args);
	}

}
