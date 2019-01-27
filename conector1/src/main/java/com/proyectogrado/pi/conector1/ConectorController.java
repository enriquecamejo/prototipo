package com.proyectogrado.pi.conector1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@EnableIntegration
@Component
public class ConectorController {
	
	@Autowired
	Environment env;
		
	private Logger logger = LoggerFactory.getLogger(ConectorController.class);
	
	private Map<String, Object> replyChannelMap = new ConcurrentHashMap<String, Object>();

    public String captureReplyChannel(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Object channel = headers.getReplyChannel();
        String correlationID = Utils.getRandomHexString(20);
        logger.info("request correlationID: "+correlationID);
        logger.info("request: "+message.getPayload().toString());
        replyChannelMap.put(correlationID, channel);
        return correlationID;
    }

    public Object getReplyChannel(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Object correlationID = headers.get("idMensaje");        
        if (correlationID == null || ! (correlationID instanceof String))
            return null;

        String id = (String) correlationID;        
        
        logger.info("response correlationID: "+correlationID);
        logger.info("response: "+message.getPayload().toString());
        
        Object channel = replyChannelMap.remove(id);
        return channel;
    }
    
    public String prueba(Message<?> message) {
    	
    	String tipoComposicion = env.getProperty("solucion.s1.tipoComposicion");
    	
    	MessageHeaders headers = message.getHeaders();
        logger.info("headers "+headers.toString());
    	
    	return new String("dd"); 
    }
    public String setTipoComposicion(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        logger.info("headers "+headers.toString());
        String idSol = (String) headers.get("idsol");
        
        StringBuffer tipoComposicionProperty = new StringBuffer("solucion.").append(idSol).append(".tipoComposicion");
		String tipoComposicion = env.getProperty(tipoComposicionProperty.toString());
        
		logger.info("El tipo de composicion para la solucion "+idSol+" es :"+tipoComposicion);
		return tipoComposicion;
    }
	
}
