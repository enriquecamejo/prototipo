package com.proyectogrado.pi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ClienteController {

	Map<String, String> repositorio = new HashMap<String, String>();
	
	@RequestMapping(value = "/respuestaSolicitud",produces = { "application/json" },consumes = { "application/json" },
    method = RequestMethod.POST)
	@ResponseBody
    public Solicitud solicitudes(@RequestBody Solicitud sol) {
		
		repositorio.put(sol.getIdMensaje(), sol.toString());
		System.out.println(sol.toString());
        return sol;
    }
	
	@GetMapping("/todos")
    public String imprimirTodos() {
		//final String respuesta = "";
		repositorio.forEach((id, sol) -> System.out.println(sol.toString()));
    	//System.out.println("");
        return repositorio.toString();
    }
	
}