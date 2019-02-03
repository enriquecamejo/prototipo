package com.proyectogrado.pi.orquestador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import utils.MensajeCanonicoUtils;

@RestController
public class OrquestadorController {
	
	@Autowired
	Environment env;
	
	@Autowired
	ClientOrquestadorService clientOrquestador;
	
	private Logger logger = LoggerFactory.getLogger(OrquestadorController.class);
	
	@RequestMapping("/orquestador/ejecutarSolucion")
    @ResponseBody
	public String ejecutarSolucion(@RequestBody String contenidoMensaje, @RequestHeader String idSol) throws Exception {
		try {
			StringBuffer itinerarioPrpty = new StringBuffer("solucion.");
			itinerarioPrpty.append(idSol).append(".itinerario");
	        String itinerarioSolucion = env.getProperty(itinerarioPrpty.toString());
	        String[] listaItinerario = itinerarioSolucion.split(",");
	        int paso = 0;
	        Message<String> mensaje;
	        String resultado;
	        for (int i = 0; i< listaItinerario.length; i++) {
	        	String nombreMicroservicio = listaItinerario[i];
	        	clientOrquestador.setServiceUrl(nombreMicroservicio);
	        	paso = paso + 1;
	    		mensaje = MessageBuilder.withPayload(contenidoMensaje).setHeader("idSol", idSol).setHeader("paso", paso).build();
	        	try {
	        		resultado = clientOrquestador.ejecutarServicio(mensaje);
	        	}catch (Exception e) {
	        		logger.error("ERROR EN ORQUESTADOR: El componente "+nombreMicroservicio+" dió el error: "+e.getMessage());
	    			return "No se pudo ejecutar la operación. Vuelva a intentarlo más tarde.";
				}
	        	String statusResponse = MensajeCanonicoUtils.obtenerHeaderMensajeCanonico("status", resultado);
	    		logger.info("STATUS MENSAJE="+statusResponse);
	    		contenidoMensaje = MensajeCanonicoUtils.obtenerPayloadMensajeCanonico(resultado);
	    		if (!"200".equals(statusResponse)) {
	    			logger.error("ERROR EN ORQUESTADOR: El componente "+nombreMicroservicio+" respondió con código de error "+statusResponse);
	    			return contenidoMensaje;
	    		}
	        }
			return contenidoMensaje;
		}catch(Exception ex) {
			logger.error("ERROR EN ORQUESTADOR: "+ex.getMessage());
			return "No se pudo ejecutar la operación. Vuelva a intentarlo más tarde.";
		}
	}

}
