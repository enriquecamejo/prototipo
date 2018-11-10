package com.proyecto.pi.clienteFinalSOAP;

public class PersonaDto {
	
	private String ci;
	private String nombre;
	private String apellido;
	private int edad;
	
	public PersonaDto() {
		super();
	}

	public PersonaDto(String ci, String nombre, String apellido, int edad) {
		super();
		this.ci = ci;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}
	
}
