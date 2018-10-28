package com.proyectogrado.pi.transformacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.SendTo;


@EnableBinding(TransformacionProcesador.class)
public class TransformacionProcessor<T> {
		
	@Autowired
	TransformacionLogica transformacionLogica;
	
	@Autowired
	Environment env;
	
	@Autowired
	TransformacionProcesador transformacionProcesador;
	
	private Logger logger = LoggerFactory.getLogger(TransformacionProcessor.class);
		
	@StreamListener(target = "transformacionSubscribableChannel")
	//@SendTo(Processor.OUTPUT)
	public void receive(Message<String> message) throws Exception {
		logger.info("Mensaje recibido en el Transformador: "+message.toString());
		MessageHeaders headers = message.getHeaders();
		String idSol = (String) headers.get("idSol");
		Integer paso = (Integer) headers.get("paso");
		StringBuffer tipoComSolPrpty = new StringBuffer("solucion.tipoComunicacion.").append(idSol);
		String tipoComunicacionSol = env.getProperty(tipoComSolPrpty.toString());	
		int numero = (int) (Math.random() * 100);
		logger.info("VAMOS A TRANSFORMAR!! El numero aleatorio es:"+numero);
		String idMensaje = (String) headers.get("idMensaje");
		Message<String> messageResultado;
		if (numero > 70) {
			logger.error("El transformador dio error!!");
			if ("req-resp".equals(tipoComunicacionSol)) {
				String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
				messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).setHeader("idSol", idSol).setHeader("paso", paso).setHeader("idMensaje", idMensaje).build();
				transformacionProcesador.transformacionMessagesErrores().send(messageResultado);
				//enviarMensajeError(messageResultado);
			}
			throw new Exception();
		}
		String result = transformacionLogica.transformacionXSLT(message.getPayload(), idSol, paso);
		logger.info("Resultado de Transformacion: "+result);
        paso = paso + 1;
        
        messageResultado = (Message<String>) MessageBuilder.withPayload(result).setHeader("idSol", idSol).setHeader("paso", paso).setHeader("idMensaje", idMensaje).build();
        transformacionProcesador.transformacionMessages().send(messageResultado);
        //enviarMensajeCorrecto(messageResultado);
        //return messageResultado;
	}
	
	
	@SendTo("transformacionMessagesChannel")
	public Message<String> enviarMensajeCorrecto(Message<String> mensaje) {
		return mensaje;
	}
	
	@SendTo("transformacionMessagesChannelErrores")
	public Message<String> enviarMensajeError(Message<String> mensaje) {
		return mensaje;
	}
	

}
