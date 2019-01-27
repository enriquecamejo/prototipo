package com.proyectogrado.pi.conector1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:http-inbound-gateway.xml"})
public class ConectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConectorApplication.class, args);
	}
}
