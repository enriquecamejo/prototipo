package utils;

import java.util.Map;

import org.json.JSONObject;

public interface MensajeCanonicoUtils {
	
	public static String crearMensajeCanonico(Map<String,String> headers, String payload){
		JSONObject json = new JSONObject();
		JSONObject jsonHeaders = new JSONObject();
		for(Map.Entry<String, String> header : headers.entrySet()){
			jsonHeaders.put(header.getKey(), header.getValue());
		}
		json.put("headers", jsonHeaders);
		json.put("payload", payload);
		
		return json.toString();			
	}
	
	public static String obtenerHeaderMensajeCanonico(String header, String mensaje) {
		JSONObject jsonObj = new JSONObject(mensaje);
		JSONObject headersObj = jsonObj.getJSONObject("headers");
		return headersObj.getString(header);

	}
	
	public static String obtenerPayloadMensajeCanonico(String mensaje) {
		JSONObject jsonObj = new JSONObject(mensaje);
		return jsonObj.getString("payload");

	}

}
