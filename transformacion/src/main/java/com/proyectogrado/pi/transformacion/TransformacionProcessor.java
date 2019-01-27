package com.proyectogrado.pi.transformacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;


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
	public void receive(Message<String> message) throws Exception {
		try {
			logger.info("Mensaje recibido en el Transformador: "+message.toString());
			MessageHeaders headers = message.getHeaders();
			String idSol = (String) headers.get("idsol");
			int paso = Integer.parseInt((String) headers.get("paso"));
			
			StringBuffer tipoComSolPrpty = new StringBuffer("solucion.").append(idSol).append(".tipoComunicacion");
			String tipoComunicacionSol = env.getProperty(tipoComSolPrpty.toString());	
			int numero = (int) (Math.random() * 100);
			logger.info("VAMOS A TRANSFORMAR!! El numero aleatorio es:"+numero);
			String idMensaje = (String) headers.get("idMensaje");
			Message<String> messageResultado;
			
			if (numero > 100) {
				logger.error("El transformador dio error!!");
				if ("req-resp".equals(tipoComunicacionSol)) {
					String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
					messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).setHeader("idSol", idSol).setHeader("paso", paso).setHeader("idMensaje", idMensaje).build();
					transformacionProcesador.transformacionMessagesErrores().send(messageResultado);
				}
				throw new Exception("Error por número aleatorio!!");
			}
			String result = transformacionLogica.transformacionXSLT(message.getPayload(), idSol, paso);
			logger.info("Resultado de Transformacion: "+result);
	        paso = paso + 1;
	        
	        messageResultado = (Message<String>) MessageBuilder.withPayload(result).setHeader("idSol", idSol).setHeader("paso", paso).setHeader("idMensaje", idMensaje).build();
	        transformacionProcesador.transformacionMessages().send(messageResultado);
	        logger.info("Se ejecutó TRANSFORMADOR exitosamente");
		}catch(Exception ex) {
			logger.error("ERROR EN TRANSFORMADOR: "+ex.getMessage());
		}
	}
	
//	@StreamListener(target = "transformacionReplySubscribableChannel")
//	public void receiveReply(Message<String> message) {
//		try {
//			Message<String> messageResultado;
//			messageResultado = procesamientoTransformacion(message);
//	        transformacionProcesador.transformacionMessagesErrores().send(messageResultado);
//	        logger.info("Se ejecutó TRANSFORMADOR exitosamente");
//		}catch(Exception ex) {
//			logger.error("ERROR EN TRANSFORMADOR: "+ex.getMessage());
//			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
//			Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
//			transformacionProcesador.transformacionMessagesErrores().send(messageResultado);
//		}
//	}
	
	
//	private Message<String> procesamientoTransformacion(Message<String> message) throws Exception {
//		logger.info("Mensaje recibido en el Transformador: "+message.toString());
//		MessageHeaders headers = message.getHeaders();
//		String idSol = (String) headers.get("idsol");
//		int paso = Integer.parseInt((String) headers.get("paso"));
//		int numero = (int) (Math.random() * 100);
//		logger.info("VAMOS A TRANSFORMAR!! El numero aleatorio es:"+numero);
//		Message<String> messageResultado;
//		if (numero > 80) {
//			logger.error("El transformador dio error!!");
//			throw new Exception("Error por número aleatorio!!");
//		}
//		String result = transformacionLogica.transformacionXSLT(message.getPayload(), idSol, paso);
//		logger.info("Resultado de Transformacion: "+result);
//        paso = paso + 1;
//        messageResultado = (Message<String>) MessageBuilder.withPayload(result).copyHeaders(message.getHeaders()).setHeader("paso", paso).build();
//        return messageResultado;
//	}
	
		

}
