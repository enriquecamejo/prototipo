package com.proyectogrado.pi;

import java.util.ArrayList;
import java.util.List;

public class ServicioPersona {
	
	private static List<Persona> personas;
	
    public ServicioPersona() {
        inicializarPersonas();
    }
    
    public Persona buscarPorCI(String ci) throws Exception {
    	for (Persona p : personas) {
    		if (p.getCi().equals(ci)) {
    			return p;
    		}
    	}
        throw new Exception("No se encontró persona con CI " + ci);
    }
    
    public Persona[] obtenerTodasPersonas() {
    	Persona[] resultado = new Persona[personas.size()];
        int i = 0;
        for (Persona p : personas) {
        	resultado[i] = p;
            i++;
        }
        return resultado;
    }
    
    public void insertarPersona(Persona persona) {
    	personas.add(persona);
    }

	private void inicializarPersonas() {
		if (personas == null) {
			personas = new ArrayList<Persona>();
			personas.add(new Persona("1111111-1", "Juan", "Perez", 20));
			personas.add(new Persona("2222222-2", "Maria", "Gomez", 26));
			personas.add(new Persona("1234567-8", "Federico", "Martinez", 50));
		}
		
	}

}
