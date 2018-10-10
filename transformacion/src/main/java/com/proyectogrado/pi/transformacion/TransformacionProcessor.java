package com.proyectogrado.pi.transformacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
		String result = transformacionLogica.transformacion(message.getPayload(), "s2", 1);
        System.out.println("RESULT="+result);
        Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(result).build();
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
