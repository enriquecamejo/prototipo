package com.proyectogrado.pi.orquestador;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientOrquestadorService {
	
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(ClientOrquestadorService.class.getName());

	/*public ClientOrquestadorService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl: "http://" + serviceUrl;
	}*/
			
	public ClientOrquestadorService() {
	}
	
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl: "http://" + serviceUrl;
	}

	public Message<String> ejecutarServicio(Message<String> message){
		//HttpEntity<Message<String>> entity = new HttpEntity<Message<String>>(message);
		//ResponseEntity<String> resultado = restTemplate.exchange(serviceUrl + "/ejecutar", HttpMethod.GET, null, String.class);
		String resultado = restTemplate.getForObject(serviceUrl + "/ejecutar", String.class);
		System.out.println("RESPUESTA!!!!="+resultado);
		return null;
	}

}
