package com.proyectogrado.pi.transformacion;

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
		
	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<String> receive(Message<String> message) {
		System.out.println("Received: " + message.toString());
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idSol");
		Integer paso = (Integer) headers.get("paso");
		String result = transformacionLogica.transformacion(message.getPayload(), idSol, paso);
        System.out.println("RESULT="+result);
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
