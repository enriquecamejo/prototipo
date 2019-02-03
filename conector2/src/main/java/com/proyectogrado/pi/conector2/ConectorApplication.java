package com.proyectogrado.pi.conector2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConectorApplication.class, args);
	}
}
