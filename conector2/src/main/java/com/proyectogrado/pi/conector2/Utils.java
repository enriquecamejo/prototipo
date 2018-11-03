package com.proyectogrado.pi.conector2;

import org.json.JSONObject;
import org.json.XML;

public interface Utils {
	
	public static String transformarXmlToJson(String texto) throws Exception{
		JSONObject xmlJSONObj = XML.toJSONObject(texto);
        return xmlJSONObj.toString(4);
    }

}
