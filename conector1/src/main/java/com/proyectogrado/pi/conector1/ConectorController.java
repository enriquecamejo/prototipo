package com.proyectogrado.pi.conector1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ConectorController {
	
	@Autowired
    ConectorSource conectorSource;
	
	private Logger logger = LoggerFactory.getLogger(ConectorController.class);
	
	@RequestMapping("/recibirMensaje")
    @ResponseBody
	public String reciveMessage(@RequestBody String contentMessage, @RequestHeader String idSol) throws Exception {
		logger.info("Llegó el siguiente mensaje a Conector1: "+contentMessage);
		int numero = (int) (Math.random() * 100);
		logger.info("EJECUTANDO CONECTOR1!! El numero aleatorio es:"+numero);
		if (numero > 70) {
			logger.error("El conector1 dió error!!");
			throw new Exception();
		}
		conectorSource.conector1Messages().send(MessageBuilder.withPayload(contentMessage).setHeader("idSol", idSol).setHeader("paso", 1).build());
		return "Mensaje recibido!";
	}

}
