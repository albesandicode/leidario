package com.example.leidario.libro;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

	private final LibroService libroService;

	public LibroController(LibroService libroService) {
		this.libroService = libroService;
	}

	@PostMapping
	public Libro guardar(@RequestBody Libro libro) {
		return libroService.guardar(libro);
	}

	@GetMapping
	public List<Libro> listar() {
		return libroService.listar();
	}
}
