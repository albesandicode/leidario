package com.example.leidario.libro;

import java.time.LocalDate;
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

	// Orden de la lista: primero los LEYENDO, luego los LEIDO y al final los QUIERO_LEER
	private static final List<EstadoLibro> ORDEN_ESTADOS =
			List.of(EstadoLibro.LEYENDO, EstadoLibro.LEIDO, EstadoLibro.QUIERO_LEER);

	@GetMapping
	public List<Libro> listar() {
		return libroService.listar().stream()
				.sorted(Comparator.comparingInt((Libro libro) -> ORDEN_ESTADOS.indexOf(libro.getEstado()))
						.thenComparing(LibroController::dentroDelEstado))
				.toList();
	}

	// Dentro de cada estado (los dos libros tienen el mismo estado):
	// LEYENDO, id de menor a mayor; LEIDO, fecha fin más reciente primero y sin fecha al final;
	// QUIERO_LEER, el último añadido (id más alto) primero
	private static int dentroDelEstado(Libro a, Libro b) {
		return switch (a.getEstado()) {
			case LEYENDO -> a.getId().compareTo(b.getId());
			case LEIDO -> Comparator.nullsLast(Comparator.<LocalDate>reverseOrder())
					.compare(a.getFechaFin(), b.getFechaFin());
			case QUIERO_LEER -> b.getId().compareTo(a.getId());
		};
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
