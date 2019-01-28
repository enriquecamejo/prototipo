package com.proyectogrado.pi.conector1;

import java.util.HashMap;
import java.util.Map;

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

import com.proyectogrado.pi.conector1.GatewayApplication.StreamGateway;

import utils.MensajeCanonicoUtils;


@RestController
public class ConectorController {
	
	@Autowired
    StreamGateway gateway;
				
	@Autowired
	Environment env;
			
	private Logger logger = LoggerFactory.getLogger(ConectorController.class);
	
	@RequestMapping("/recibirMensaje")
    @ResponseBody
	public String reciveMessage(@RequestBody String contentMessage, @RequestHeader String idSol) throws Exception {
		try {
			logger.info("Llegó el siguiente mensaje a Conector1: "+contentMessage);
			int numero = (int) (Math.random() * 100);
			logger.info("EJECUTANDO CONECTOR1!! El numero aleatorio es:"+numero);
			if (numero > 80) {
				logger.error("El conector1 dio error!!");
				return "Error de procesamiento! Consulte al administrador de la plataforma.";
			}
//			Map<String,String> headers = new HashMap<String, String>();
//			headers.put("idSol", idSol);
//			headers.put("paso", "1");
//			String mensaje = MensajeCanonicoUtils.crearMensajeCanonico(headers, contentMessage);
			Message<String> mensaje = MessageBuilder.withPayload(contentMessage).setHeader("idSol", idSol).setHeader("paso", 1).build();
			String respuesta = gateway.process(mensaje);
			logger.info("Se ejecutó CONECTOR1 exitosamente");
			return respuesta;
		}catch(Exception ex) {
			logger.error("ERROR en CONECTOR1: "+ex.getMessage());
			return "Error de procesamiento! Consulte al administrador de la plataforma.";
		}
	}	
	
}
