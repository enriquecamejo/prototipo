package com.proyectogrado.pi.conector2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;


@EnableBinding(ConectorSink.class)
public class ConectorConsumer {
	
	@Autowired
	Environment env;
	
	private Logger logger = LoggerFactory.getLogger(ConectorConsumer.class);

	@StreamListener(target = "correctasChannel")
	public void receive(Message<String> message) throws Exception{
		int numero = (int) (Math.random() * 100);
		logger.info("EJECUTANDO CONECTOR2!! El numero aleatorio es:"+numero);
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idSol");
		Integer paso = (Integer) headers.get("paso");
		StringBuffer tipoComSolPrpty = new StringBuffer("solucion.tipoComunicacion.").append(idSol);
		String tipoComunicacionSol = env.getProperty(tipoComSolPrpty.toString());
		if (numero > 70) {
			logger.error("El conector2 dio error!!");
			if ("req-resp".equals(tipoComunicacionSol)) {
				String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
				enviarNotificacion(message, msjError);
			}
			throw new Exception();
		}
		logger.info("Llego el siguiente mensaje al Conector2: "+ message.getPayload().toString());
		logger.info("Solucion ejecutada en Conector2: "+ idSol);
		logger.info("Paso de solucion ejecutada en Conector2: "+ paso);
		if ("req-resp".equals(tipoComunicacionSol)) {
			enviarNotificacion(message, "Notificar el cliente el siguiente mensaje CORRECTO----->");
		}
	}
	
	@StreamListener(target = "erroresChannel")
	public void receiveErrors(Message<String> message) throws Exception{
		enviarNotificacion(message, "Notificar el cliente el siguiente mensaje de ERROR----->");
	}
	
	private void enviarNotificacion(Message<String> message, String respuesta) {
		String idMensaje = (String) message.getHeaders().get("idMensaje");
		logger.error(respuesta + message.getPayload());
		logger.error("Id Mensaje----->"+ idMensaje);
	}
}
