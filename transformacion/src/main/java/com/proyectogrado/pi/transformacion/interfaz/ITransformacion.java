package com.proyectogrado.pi.transformacion.interfaz;

import org.springframework.messaging.Message;

public interface ITransformacion<T> {
	
	Message<T> logicaTransformacion(Message<T> message);
	
}
