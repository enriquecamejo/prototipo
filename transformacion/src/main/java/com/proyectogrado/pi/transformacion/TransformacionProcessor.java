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
	
	@Value("${transformacion.class}")
    private String name;
	
	@Autowired
	private TransformacionLogica transformacionLogica;
	
	//private ITransformacion<String> transformacion;
	//private final Log logger = LogFactory.getLog(getClass());
	
	/*@Autowired
	public void setTransformacion(ApplicationContext context) {
		this.transformacion = (ITransformacion<String>) context.getBean(name);
	}*/
	
	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<String> receive(Message<String> message) {
		System.out.println("Received: " + message.toString());
		String result = transformacionLogica.transformacion(message.getPayload(), "s1", 1);
        System.out.println("RESULT="+result);
        Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(result).build();
        return messageResultado;
		//return transformacion.logicaTransformacion(message);
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
