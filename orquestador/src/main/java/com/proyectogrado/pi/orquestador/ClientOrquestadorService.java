package com.proyectogrado.pi.orquestador;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import utils.MensajeCanonicoUtils;

@Service
public class ClientOrquestadorService {
	
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(ClientOrquestadorService.class.getName());
			
	public ClientOrquestadorService() {
	}
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl: "http://" + serviceUrl;
	}

	public String ejecutarServicio(Message<String> message){
		MessageHeaders headersMessage = message.getHeaders();
		String idSol = (String) headersMessage.get("idSol");
		Integer paso = (Integer) headersMessage.get("paso");
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("idSol", idSol);
		headers.add("paso", String.valueOf(paso));
		HttpEntity<String> entity = new HttpEntity<String>(message.getPayload(), headers);
		ResponseEntity<String> resultado = restTemplate.exchange(serviceUrl + "/ejecutar", HttpMethod.POST, entity, String.class);
		System.out.println("RESPUESTA!!!!="+resultado.getBody());
		return resultado.getBody();
	}

}
