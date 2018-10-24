package com.proyectogrado.pi.transformacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;



@EnableBinding(Processor.class)
public class TransformacionProcessor<T> {
		
	@Autowired
	private TransformacionLogica transformacionLogica;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionProcessor.class);
		
	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<String> receive(Message<String> message) throws Exception {
		logger.info("Mensaje recibido en el Transformador: "+message.toString());
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idSol");
		Integer paso = (Integer) headers.get("paso");
		int numero = (int) (Math.random() * 100);
		logger.info("VAMOS A TRANSFORMAR!! El numero aleatorio es:"+numero);
		if (numero > 70) {
			logger.error("El transformador dio error!!");
			throw new Exception();
		}
		String result = transformacionLogica.transformacionXSLT(message.getPayload(), idSol, paso);
		logger.info("Resultado de Transformacion: "+result);
        paso = paso + 1;
        Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(result).setHeader("idSol", idSol).setHeader("paso", paso).build();
        return messageResultado;
	}
		

	public interface Sink {
		@Input("transformacionSubscribableChannel")
		SubscribableChannel transformacionSubscribable();
	}

	public interface Source {
		@Output("transformacionMessagesChannel")
		MessageChannel transformacionMessages();
	}

}
