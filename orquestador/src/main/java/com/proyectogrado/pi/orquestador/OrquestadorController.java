package com.proyectogrado.pi.orquestador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrquestadorController {
	
	@Autowired
	Environment env;
	
	@Autowired
	ClientOrquestadorService clientOrquestador;
	

	@RequestMapping("/orquestador/ejecutarSolucion")
    @ResponseBody
	public String ejecutarSolucion(@RequestBody String contentMessage, @RequestHeader String idSol) throws Exception {
		StringBuffer itinerarioPrpty = new StringBuffer("solucion.");
		itinerarioPrpty.append(idSol).append(".itinerario");
        String itinerarioSolucion = env.getProperty(itinerarioPrpty.toString());
        String[] listaItinerario = itinerarioSolucion.split(",");
        int paso = 1;
        Message<String> mensaje = MessageBuilder.withPayload(contentMessage).setHeader("idSol", idSol).setHeader("paso", paso).build();
        for (int i = 0; i< listaItinerario.length; i++) {
        	String nombreMicroservicio = listaItinerario[i];
        	clientOrquestador.setServiceUrl(nombreMicroservicio);
        	mensaje = clientOrquestador.ejecutarServicio(mensaje);
        }
		return null;
	}

}
