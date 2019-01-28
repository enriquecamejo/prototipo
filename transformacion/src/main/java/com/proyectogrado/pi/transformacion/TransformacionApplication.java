package com.proyectogrado.pi.transformacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class TransformacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransformacionApplication.class, args);
	}
}
