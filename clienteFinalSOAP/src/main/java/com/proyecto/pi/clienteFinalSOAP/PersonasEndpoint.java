package com.proyecto.pi.clienteFinalSOAP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import clientefinalsoap.personas.BuscarPorCIRequest;
import clientefinalsoap.personas.BuscarPorCIResponse;
import clientefinalsoap.personas.Persona;

@Endpoint
public class PersonasEndpoint {
	
	@Autowired
	ServicioPersona sevicioPersona;

	@PayloadRoot(namespace = "http://clienteFinalSOAP/personas", localPart = "buscarPorCIRequest")
	@ResponsePayload
	public BuscarPorCIResponse buscarPorCI(@RequestPayload BuscarPorCIRequest request) {
		BuscarPorCIResponse response = new BuscarPorCIResponse();
		try {
		    String ci = request.getCi();
			PersonaDto personaDto;
			personaDto = sevicioPersona.buscarPorCI(ci);
			Persona persona = new Persona();
			persona.setCi(personaDto.getCi());
			persona.setNombre(personaDto.getNombre());
			persona.setApellido(personaDto.getApellido());
			persona.setEdad(personaDto.getEdad());
		    response.setPersona(persona);
		    response.setCodigoRespuesta(0);
		} catch (Exception e) {
			response.setCodigoRespuesta(1);
			response.setMensajeRespuesta(e.getMessage());
		}
		return response;
	}
}
