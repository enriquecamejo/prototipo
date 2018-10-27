package com.proyectogrado.pi.conector2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;


@EnableBinding(ConectorSink.class)
public class ConectorConsumer {
	
	private Logger logger = LoggerFactory.getLogger(ConectorConsumer.class);

	@StreamListener(target = "correctasChannel")
	public void receive(Message<String> message) throws Exception{
		int numero = (int) (Math.random() * 100);
		logger.info("EJECUTANDO CONECTOR2!! El numero aleatorio es:"+numero);
		if (numero > 70) {
			logger.error("El conector2 dio error!!");
			throw new Exception();
		}
		logger.info("Llego el siguiente mensaje al Conector2: "+ message.getPayload().toString());
		logger.info("Solucion ejecutada en Conector2: "+ message.getHeaders().get("idSol"));
		logger.info("Paso de solucion ejecutada en Conector2: "+ message.getHeaders().get("paso"));
	}
	
	@StreamListener(target = "erroresChannel")
	public void receiveErrors(Message<String> message) throws Exception{
		enviarNotificacion(message);
	}
	
	private void enviarNotificacion(Message<String> message) {
		String idMensaje = (String) message.getHeaders().get("idSol");
		logger.error("Notificar el cliente el siguiente mensaje de ERROR----->"+ message.getPayload());
		logger.error("Id Mensaje----->"+ idMensaje);
	}
}
