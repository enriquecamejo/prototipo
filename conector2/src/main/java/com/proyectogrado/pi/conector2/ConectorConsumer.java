package com.proyectogrado.pi.conector2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;


@EnableBinding(ConectorSink.class)
public class ConectorConsumer {
	
	@Autowired
	Environment env;
	
	@Autowired
	ConectorSink conectorSink;
	
	@Autowired
	ServicioParametrosRequestSOAP servicioParametrosRequestSOAP;
	
	@Autowired
	ConectorLogica conectorLogica;
		
	private Logger logger = LoggerFactory.getLogger(ConectorConsumer.class);

	@StreamListener(target = "correctasChannel")
	public void receive(Message<String> message){
		try {
			Message<String> mensaje = conectorLogica.procesamientoConector(message);
				
			MessageHeaders headers = message.getHeaders();
			String idSol = (String) headers.get("idSol");
			StringBuffer tipoComSolPrpty = new StringBuffer("solucion.").append(idSol).append(".tipoComunicacion");
			String tipoComunicacionSol = env.getProperty(tipoComSolPrpty.toString());
			
			if ("req-resp".equals(tipoComunicacionSol)) {
				conectorSink.conector2Reply().send(mensaje);
			}else {
				conectorSink.respuestas().send(message);
			}
			logger.info("Se ejecutó CONECTOR2 exitosamente");
		}catch(Exception ex) {
			logger.error("ERROR EN CONECTOR2:"+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			conectorSink.respuestas().send(messageResultado);
		}
	}
	

	/*@StreamListener(target = "erroresChannel")
	public void receiveErrors(Message<String> message) throws Exception{
		try {
			MessageHeaders headers = message.getHeaders();
			String idSol = (String) headers.get("idSol");
			logger.error("Notificar el cliente el siguiente mensaje de ERROR----->"+ message.getPayload());
			enviarNotificacion(message, "Notificar el cliente el siguiente mensaje de ERROR----->"+ message.getPayload(), idSol);
		}catch(Exception ex) {
			logger.error("ERROR EN CONECTOR2:"+ex.getMessage());
		}
	}*/
	
//	private void enviarNotificacion(Message<String> message, String respuesta, String idSol) {
//		try {
//			String idMensaje = (String) message.getHeaders().get("idMensaje");
//			logger.info("Id Mensaje----->"+ idMensaje);
//			StringBuffer clienteURL = new StringBuffer("solucion.").append(idSol).append(".clienteURL");
//			String clienteConnection = env.getProperty(clienteURL.toString());
//			URL url = new URL(clienteConnection);
//	        URLConnection uc = url.openConnection();
//	        HttpURLConnection conn = (HttpURLConnection) uc;
//	        conn.setDoOutput(true);
//	        conn.setRequestProperty("content-type", "application/json");
//	        conn.setRequestMethod("POST");
//			OutputStream os = conn.getOutputStream();
//			String texto = construirRespuestaCliente(idMensaje, respuesta);
//			os.write(texto.getBytes());
//			os.flush();
//			os.close();
//	        
//	        int rspCode = conn.getResponseCode();
//	        if (rspCode == HttpURLConnection.HTTP_OK) {
//	        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				String inputLine;
//				StringBuffer response = new StringBuffer();
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//				logger.info("Notificacion en CONECTOR2 exitosa: " + response.toString());
//	        }else {
//				logger.error("ERROR en CONECTOR2 al notificar: POST request no funcionó");
//			}
//		} catch (Exception e) {
//			logger.error("ERROR en CONECTOR2 al notificar:"+e.getMessage());
//		}
//	}
//
//	private String construirRespuestaCliente(String idMensaje, String respuesta) throws Exception {
//		StringBuffer msjResp = new StringBuffer("{\"idMensaje\":\"").append(idMensaje).append("\"")
//			.append(", \"respuesta\":\"").append(respuesta).append("\"}");
//		
//		return msjResp.toString();
//	}
}
