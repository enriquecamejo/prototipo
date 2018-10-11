package com.proyectogrado.pi.conector1;

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
	
	@RequestMapping("/recibirMensaje")
    @ResponseBody
	public String reciveMessage(@RequestBody String contentMessage, @RequestHeader String idSol) {
		conectorSource.conector1Messages().send(MessageBuilder.withPayload(contentMessage).setHeader("idSol", idSol).setHeader("paso", 1).build());
		System.out.println(contentMessage);
		return "Mensaje recibido!";
	}

}
