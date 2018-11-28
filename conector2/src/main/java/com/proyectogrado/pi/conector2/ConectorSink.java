package com.proyectogrado.pi.conector2;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ConectorSink {
	
	@Input("correctasChannel")
	SubscribableChannel correctas();
	
	@Output("respuestasChannel")
	MessageChannel respuestas();
	
	@Output("conector2ReplyChannel")
	MessageChannel conector2Reply();

}
