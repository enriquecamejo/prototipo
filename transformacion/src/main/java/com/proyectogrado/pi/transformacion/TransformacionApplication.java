package com.proyectogrado.pi.transformacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TransformacionApplication {

	public static void main(String[] args) {
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-bean-cfg.xml");
		SpringApplication.run(TransformacionApplication.class, args);
	}
}
