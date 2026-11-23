package com.example.leidario.libro;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LibroService {

	private final LibroRepository libroRepository;

	public LibroService(LibroRepository libroRepository) {
		this.libroRepository = libroRepository;
	}

	public Libro guardar(Libro libro) {
		return libroRepository.save(libro);
	}

	public List<Libro> listar() {
		return libroRepository.findAll();
	}
}
