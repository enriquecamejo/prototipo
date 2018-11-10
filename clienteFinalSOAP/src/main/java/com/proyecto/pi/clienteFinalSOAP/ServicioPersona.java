package com.proyecto.pi.clienteFinalSOAP;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ServicioPersona {
	
	private static List<PersonaDto> personas;
	
    public ServicioPersona() {
        inicializarPersonas();
    }
    
    public PersonaDto buscarPorCI(String ci) throws Exception {
    	for (PersonaDto p : personas) {
    		if (p.getCi().equals(ci)) {
    			return p;
    		}
    	}
        throw new Exception("No se encontró persona con CI " + ci);
    }
    
    public PersonaDto[] obtenerTodasPersonas() {
    	PersonaDto[] resultado = new PersonaDto[personas.size()];
        int i = 0;
        for (PersonaDto p : personas) {
        	resultado[i] = p;
            i++;
        }
        return resultado;
    }
    
    public void insertarPersona(PersonaDto persona) {
    	personas.add(persona);
    }

	private void inicializarPersonas() {
		if (personas == null) {
			personas = new ArrayList<PersonaDto>();
			personas.add(new PersonaDto("1111111-1", "Juan", "Perez", 20));
			personas.add(new PersonaDto("2222222-2", "Maria", "Gomez", 26));
			personas.add(new PersonaDto("1234567-8", "Federico", "Martinez", 50));
		}
		
	}

}
