package com.example.leidario.libro;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

	// Si fallan varios campos, se informa del primero en este orden
	private static final List<String> ORDEN_CAMPOS = List.of("titulo", "estado", "nota");

	private final LibroService libroService;

	public LibroController(LibroService libroService) {
		this.libroService = libroService;
	}

	// @Valid: si el libro no cumple las reglas de Libro, no se llega a llamar al servicio
	@PostMapping
	public Libro guardar(@Valid @RequestBody Libro libro) {
		return libroService.guardar(libro);
	}

	@GetMapping
	public List<Libro> listar() {
		return libroService.listar();
	}

	// Libro no válido → 400 con { "mensaje": "..." } del campo que falla
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> libroNoValido(MethodArgumentNotValidException ex) {
		FieldError error = ex.getBindingResult().getFieldErrors().stream()
				.min(Comparator.comparingInt(e -> ORDEN_CAMPOS.indexOf(e.getField())))
				.orElseThrow();
		return Map.of("mensaje", error.getDefaultMessage());
	}
}
