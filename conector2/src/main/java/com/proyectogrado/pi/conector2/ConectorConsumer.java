package com.proyectogrado.pi.conector2;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

@EnableBinding(Sink.class)
public class ConectorConsumer {

	@StreamListener(target = Sink.INPUT)
	public void receive(Message<String> message){
		System.out.println("Received in the connector 2 - Payload: " + message.getPayload().toString());
		System.out.println("Received in the connector 2 - Header idSol: " + message.getHeaders().get("idSol"));
		System.out.println("Received in the connector 2 - Header Paso: " + message.getHeaders().get("paso"));
	}
}
