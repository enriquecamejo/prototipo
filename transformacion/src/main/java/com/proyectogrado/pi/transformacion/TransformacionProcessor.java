package com.proyectogrado.pi.transformacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;

import com.proyectogrado.pi.transformacion.interfaz.ITransformacion;

@EnableBinding(Processor.class)
public class TransformacionProcessor<T> {
	
	@Autowired
	private ITransformacion<String> transformacion;
	//private final Log logger = LogFactory.getLog(getClass());

	
	/*@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object transform(String message) {
		System.out.println("Llegué al transformador!! Soy el mensaje: "+ message);
		return message.toUpperCase();
	} */
	
	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<String> receive(Message<String> message) {
	
		System.out.println("Received: " + message.toString());
		return transformacion.logicaTransformacion(message);
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
