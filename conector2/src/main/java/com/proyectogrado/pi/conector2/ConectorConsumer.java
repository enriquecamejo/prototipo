package com.proyectogrado.pi.conector2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

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
		
	private Logger logger = LoggerFactory.getLogger(ConectorConsumer.class);

	@StreamListener(target = "correctasChannel")
	public void receive(Message<String> message){
		try {
			int numero = (int) (Math.random() * 100);
			logger.info("EJECUTANDO CONECTOR2!! El numero aleatorio es:"+numero);
			MessageHeaders headers = message.getHeaders();
			String idSol = (String) headers.get("idSol");
			Integer paso = (Integer) headers.get("paso");
			StringBuffer tipoComSolPrpty = new StringBuffer("solucion.").append(idSol).append(".tipoComunicacion");
			String tipoComunicacionSol = env.getProperty(tipoComSolPrpty.toString());
			if (numero > 80) {
				logger.error("El conector2 dio error!!");
				throw new Exception("Error por número aleatorio!!");
			}
			logger.info("Llego el siguiente mensaje al Conector2: "+ message.getPayload().toString());
			logger.info("Solucion ejecutada en Conector2: "+ idSol);
			logger.info("Paso de solucion ejecutada en Conector2: "+ paso);
			if ("req-resp".equals(tipoComunicacionSol)) {
				StringBuffer elementParam1Prpty = new StringBuffer("conectorSalida.").append(idSol).append(".requestParam1");
				String elementParam1 = env.getProperty(elementParam1Prpty.toString());
				String param1 = servicioParametrosRequestSOAP.obtenerUnParametro(elementParam1, message.getPayload());
				StringBuffer clienteWSDLPrpty = new StringBuffer("conectorSalida.").append(idSol).append(".clienteWSDL");
		        String clienteWSDL = env.getProperty(clienteWSDLPrpty.toString());
				String wsdlResponse = llamadaClienteSOAP(clienteWSDL, param1);
				logger.info("Respuesta de cliente final: "+ wsdlResponse);
				Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(wsdlResponse).copyHeaders(message.getHeaders()).build();
				conectorSink.conector2Reply().send(messageResultado);
			}else {
				conectorSink.respuestas().send(message);
			}
		}catch(Exception ex) {
			logger.error("ERROR EN CONECTOR2:"+ex.getMessage());
			String msjError = "Error de procesamiento! Consulte al administrador de la plataforma.";
			Message<String> messageResultado = (Message<String>) MessageBuilder.withPayload(msjError).copyHeaders(message.getHeaders()).build();
			conectorSink.respuestas().send(messageResultado);
		}
	}
	
	private String llamadaClienteSOAP(String url, String param) throws Exception {
		 try{
	          // Create SOAP Connection
	         SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
	         SOAPConnection soapConnection = soapConnectionFactory.createConnection();
	
	         //Send SOAP Message to SOAP Server
	         SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(param), url);
	         
	         // Process the SOAP Response
	         String resultadoXML = getContentXML(soapResponse);
	         
	         soapConnection.close();
	         return resultadoXML;
         } catch (Exception e){
        	 logger.error("Ocurrio el siguiente error al enviar request SOAP al cliente final: "+e.getMessage());
        	 throw new Exception("Error al comunicarse con el cliente final");
         }
	}

	private SOAPMessage createSOAPRequest(String param) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("per", "http://clienteFinalSOAP/personas");

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("buscarPorCIRequest", "per");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("ci", "per");
        soapBodyElem1.addTextNode(param);

        soapMessage.saveChanges();

        // Check the input
        System.out.println("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();
        return soapMessage;
	}
	
	private String getContentXML(SOAPMessage soapResponse) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapResponse.writeTo(out);
		String strMsg = new String(out.toByteArray());
		return strMsg;
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
	
	private void enviarNotificacion(Message<String> message, String respuesta, String idSol) {
		try {
			String idMensaje = (String) message.getHeaders().get("idMensaje");
			logger.info("Id Mensaje----->"+ idMensaje);
			StringBuffer clienteURL = new StringBuffer("solucion.").append(idSol).append(".clienteURL");
			String clienteConnection = env.getProperty(clienteURL.toString());
			URL url = new URL(clienteConnection);
	        URLConnection uc = url.openConnection();
	        HttpURLConnection conn = (HttpURLConnection) uc;
	        conn.setDoOutput(true);
	        conn.setRequestProperty("content-type", "application/json");
	        conn.setRequestMethod("POST");
			OutputStream os = conn.getOutputStream();
			String texto = construirRespuestaCliente(idMensaje, respuesta);
			os.write(texto.getBytes());
			os.flush();
			os.close();
	        
	        int rspCode = conn.getResponseCode();
	        if (rspCode == HttpURLConnection.HTTP_OK) {
	        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				logger.info("Notificacion en CONECTOR2 exitosa: " + response.toString());
	        }else {
				logger.error("ERROR en CONECTOR2 al notificar: POST request no funcionó");
			}
		} catch (Exception e) {
			logger.error("ERROR en CONECTOR2 al notificar:"+e.getMessage());
		}
	}

	private String construirRespuestaCliente(String idMensaje, String respuesta) throws Exception {
		StringBuffer msjResp = new StringBuffer("{\"idMensaje\":\"").append(idMensaje).append("\"")
			.append(", \"respuesta\":\"").append(respuesta).append("\"}");
		
		return msjResp.toString();
	}
}
