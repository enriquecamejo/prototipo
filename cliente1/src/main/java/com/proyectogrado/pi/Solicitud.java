package com.proyectogrado.pi;

public class Solicitud {

	private String idMensaje;
	private String respuesta;
	
	public Solicitud(String idMensaje, String respuesta) {
		this.idMensaje = idMensaje;
		this.respuesta = respuesta;
	}
		
	public Solicitud() {
	}

	public String getIdMensaje() {
		return idMensaje;
	}

	public String getRespuesta() {
		return this.respuesta;
	}
	
	@Override
	public String toString() {
		return "Solicitud [idMensaje=" + idMensaje + ", respuesta=" + respuesta + "]";
	}
}
