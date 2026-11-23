package com.example.leidario.libro;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository ya trae save, findAll, findById, deleteById...
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
