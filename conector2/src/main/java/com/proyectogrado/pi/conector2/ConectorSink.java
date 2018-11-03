package com.proyectogrado.pi.conector2;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConectorSink {
	
	@Input("correctasChannel")
	SubscribableChannel correctas();
	
	@Input("erroresChannel")
	SubscribableChannel errores();

}
