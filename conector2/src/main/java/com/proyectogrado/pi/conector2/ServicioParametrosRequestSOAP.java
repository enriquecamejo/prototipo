package com.proyectogrado.pi.conector2;

import org.springframework.stereotype.Service;

@Service
public class ServicioParametrosRequestSOAP {
	
	public String obtenerUnParametro(String param, String mensajeXML) throws Exception {
		if (mensajeXML.contains(param)) {
			String separador1 = "<"+param+">";
			String separador2 = "</"+param+">";
			String[] arr1 = mensajeXML.split(separador1);
			String[] arr2 = arr1[1].split(separador2);
			return arr2[0];
		}
		throw new Exception("Parámetro no encontrado en mensaje XML del request");
	}

}
