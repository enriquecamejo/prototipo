package com.proyectogrado.pi.transformacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransformacionController {
	
	@Autowired
	TransformacionLogica transformacionLogica;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionController.class);
	
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
    @ResponseBody
	public Message<String> ejecutarTransformacion(@RequestBody Message<String> message){
		Message<String> messageResultado;
		try {
			messageResultado = transformacionLogica.procesamientoTransformacion(message);
			logger.info("Se ejecutó TRANSFORMADOR exitosamente");
		} catch (Exception ex) {
			logger.error("ERROR EN TRANSFORMADOR: "+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
		}
		return messageResultado;
	}
	
	@RequestMapping("/ejecutar")
	public String ejecutarTrans(){
		return "Hola!";
	}

}
