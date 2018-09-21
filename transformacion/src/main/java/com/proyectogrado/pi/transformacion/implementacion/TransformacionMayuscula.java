package com.proyectogrado.pi.transformacion.implementacion;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.proyectogrado.pi.transformacion.interfaz.ITransformacion;

@Service("transformacionMayuscula")
public class TransformacionMayuscula<String> implements ITransformacion<String> {

	@Override
	public Message<String> logicaTransformacion(Message<String> message) {
		Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(message.getPayload().toString().toUpperCase()).build();
		return messageResultado;
	}

}
