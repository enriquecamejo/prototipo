package com.proyectogrado.pi.transformacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.SendTo;

import com.proyectogrado.pi.transformacion.interfaz.ITransformacion;

@EnableBinding(Processor.class)
public class TransformacionProcessor<T> {
	
	@Value("${transformacion.class}")
    private String name;
	
	private ITransformacion<String> transformacion;
	//private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public void setTransformacion(ApplicationContext context) {
		this.transformacion = (ITransformacion<String>) context.getBean(name);
	}
	
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
