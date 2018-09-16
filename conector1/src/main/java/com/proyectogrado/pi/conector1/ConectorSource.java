package com.proyectogrado.pi.conector1;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ConectorSource {
	
	@Output("conector1MessagesChannel")
    MessageChannel conector1Messages();
	
}
