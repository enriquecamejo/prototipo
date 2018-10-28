package com.proyectogrado.pi;

public class Solicitud {

	private final String id;
	private final String xml;
	private final String respuesta;
	
	public Solicitud(String id, String xml, String respuesta) {
		this.id = id;
		this.xml = xml;
		this.respuesta = respuesta;
	}
	
	public String getId() {
		return this.id;
	}
	public String getXml() {
		return this.xml;
	}
	public String getRespuesta() {
		return this.respuesta;
	}
	public String toString() {
		return "{id:"+id+",xml:"+xml+",respuesta:"+respuesta+"}";
	}
}
