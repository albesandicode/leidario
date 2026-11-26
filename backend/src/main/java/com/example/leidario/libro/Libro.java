package com.example.leidario.libro;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "libros")
public class Libro {

	// READ_ONLY: el id sale en las respuestas, pero se ignora si llega en el POST
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	// NotBlank: tampoco vale un título vacío o solo con espacios
	@NotBlank(message = "Un libro no puede ir sin título")
	private String titulo;

	private String autor;

	// STRING: guarda "LEYENDO" en la columna, no su posición (1)
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Un libro no puede ir sin estado")
	private EstadoLibro estado;

	// LocalDate: solo el día, sin hora
	private LocalDate fechaFin;

	// Opcional: si viene null no se comprueba; si viene, del 1 al 5
	@Min(value = 1, message = "La nota debe estar entre 1 y 5")
	@Max(value = 5, message = "La nota debe estar entre 1 y 5")
	private Integer nota;

	// TEXT: sin el límite de 255 caracteres de un VARCHAR por defecto
	@Column(columnDefinition = "TEXT")
	private String comentario;

	// JPA necesita un constructor sin argumentos
	public Libro() {
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public EstadoLibro getEstado() {
		return estado;
	}

	public void setEstado(EstadoLibro estado) {
		this.estado = estado;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
