package com.proyectogrado.pi.conector2;

import java.io.ByteArrayOutputStream;

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
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Service
public class ConectorLogica {
	
	@Autowired
	Environment env;
	
	@Autowired
	ServicioParametrosRequestSOAP servicioParametrosRequestSOAP;
	
	private Logger logger = LoggerFactory.getLogger(ConectorLogica.class);
	
	public Message<String> procesamientoConector(Message<String> message) throws Exception{
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
			return messageResultado;
		}else {
			return message;
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

}
