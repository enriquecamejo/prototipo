package com.proyectogrado.pi.orquestador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
//@ComponentScan(useDefaultFilters = false) // Disable component scanner
public class OrquestadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrquestadorApplication.class, args);
	}
	
	//A customized RestTemplate that has the ribbon load balancer build in
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	/*@Bean
	public ClientOrquestadorService orquestadorService() {
		return new ClientOrquestadorService("http://TRANSFORMACION1");
	}
	
	@Bean
	public OrquestadorController orquestadorController() {
		return new OrquestadorController(orquestadorService());
	}*/

}

