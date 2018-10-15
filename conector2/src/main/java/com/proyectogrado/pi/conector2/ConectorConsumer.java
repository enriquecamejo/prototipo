package com.proyectogrado.pi.conector2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;


@EnableBinding(Sink.class)
public class ConectorConsumer {
	
	private Logger logger = LoggerFactory.getLogger(ConectorConsumer.class);

	@StreamListener(target = Sink.INPUT)
	public void receive(Message<String> message) throws Exception{
		int numero = (int) (Math.random() * 100);
		logger.info("EJECUTANDO CONECTOR2!! El numero aleatorio es:"+numero);
		if (numero > 70) {
			logger.error("El conector2 dió error!!");
			throw new Exception();
		}
		logger.info("Llegó el siguiente mensaje a Conector2: "+ message.getPayload().toString());
		logger.info("Solucion ejecutada en Conector2: "+ message.getHeaders().get("idSol"));
		logger.info("Paso de solucion ejecutada en Conector2: "+ message.getHeaders().get("paso"));
	}
}
