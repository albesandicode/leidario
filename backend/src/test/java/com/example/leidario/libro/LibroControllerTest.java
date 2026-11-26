package com.example.leidario.libro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

// Solo carga la capa web (LibroController): sin base de datos
@WebMvcTest(LibroController.class)
class LibroControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private LibroService libroService;

	@Test
	void postGuardaElLibroYDevuelveTodosSusDatos() throws Exception {
		when(libroService.guardar(any(Libro.class))).thenReturn(libroDePrueba());

		String json = """
				{
				  "titulo": "El nombre del viento",
				  "autor": "Patrick Rothfuss",
				  "estado": "LEIDO",
				  "fechaFin": "2026-11-10",
				  "nota": 5,
				  "comentario": "Muy bueno."
				}
				""";

		mockMvc.perform(post("/api/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("El nombre del viento"))
				.andExpect(jsonPath("$.autor").value("Patrick Rothfuss"))
				.andExpect(jsonPath("$.estado").value("LEIDO"))
				.andExpect(jsonPath("$.fechaFin").value("2026-11-10"))
				.andExpect(jsonPath("$.nota").value(5))
				.andExpect(jsonPath("$.comentario").value("Muy bueno."));

		// El controlador pasa al servicio el libro con los datos del JSON
		ArgumentCaptor<Libro> captor = ArgumentCaptor.forClass(Libro.class);
		verify(libroService).guardar(captor.capture());
		Libro recibido = captor.getValue();
		assertThat(recibido.getTitulo()).isEqualTo("El nombre del viento");
		assertThat(recibido.getAutor()).isEqualTo("Patrick Rothfuss");
		assertThat(recibido.getEstado()).isEqualTo(EstadoLibro.LEIDO);
		assertThat(recibido.getFechaFin()).isEqualTo(LocalDate.of(2026, 11, 10));
		assertThat(recibido.getNota()).isEqualTo(5);
		assertThat(recibido.getComentario()).isEqualTo("Muy bueno.");
	}

	@Test
	void getDevuelveLaListaConElLibroDePrueba() throws Exception {
		when(libroService.listar()).thenReturn(List.of(libroDePrueba()));

		mockMvc.perform(get("/api/libros"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].titulo").value("El nombre del viento"))
				.andExpect(jsonPath("$[0].autor").value("Patrick Rothfuss"))
				.andExpect(jsonPath("$[0].estado").value("LEIDO"))
				.andExpect(jsonPath("$[0].fechaFin").value("2026-11-10"))
				.andExpect(jsonPath("$[0].nota").value(5))
				.andExpect(jsonPath("$[0].comentario").value("Muy bueno."));
	}

	@Test
	void postSinTituloDevuelve400YNoGuarda() throws Exception {
		String json = """
				{
				  "autor": "Patrick Rothfuss",
				  "estado": "LEIDO",
				  "nota": 5
				}
				""";

		mockMvc.perform(post("/api/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("Un libro no puede ir sin título"));

		verify(libroService, never()).guardar(any(Libro.class));
	}

	@Test
	void postConNota7Devuelve400YNoGuarda() throws Exception {
		String json = """
				{
				  "titulo": "El nombre del viento",
				  "estado": "LEIDO",
				  "nota": 7
				}
				""";

		mockMvc.perform(post("/api/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("La nota debe estar entre 1 y 5"));

		verify(libroService, never()).guardar(any(Libro.class));
	}

	@Test
	void postConLibroValidoLoGuarda() throws Exception {
		when(libroService.guardar(any(Libro.class))).thenReturn(libroDePrueba());

		String json = """
				{
				  "titulo": "El nombre del viento",
				  "estado": "LEIDO",
				  "nota": 5
				}
				""";

		mockMvc.perform(post("/api/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.titulo").value("El nombre del viento"));

		verify(libroService).guardar(any(Libro.class));
	}

	private Libro libroDePrueba() {
		Libro libro = new Libro();
		// Libro no tiene setId (el id lo pone la base de datos), así que se asigna por reflexión
		ReflectionTestUtils.setField(libro, "id", 1L);
		libro.setTitulo("El nombre del viento");
		libro.setAutor("Patrick Rothfuss");
		libro.setEstado(EstadoLibro.LEIDO);
		libro.setFechaFin(LocalDate.of(2026, 11, 10));
		libro.setNota(5);
		libro.setComentario("Muy bueno.");
		return libro;
	}

	@Test
	void postSinEstadoDevuelve400YNoGuarda() throws Exception {
		String json = """
				{
				  "titulo": "El nombre del viento",
				  "nota": 5
				}
				""";

		mockMvc.perform(post("/api/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("Un libro no puede ir sin estado"));

		verify(libroService, never()).guardar(any(Libro.class));
	}

	@Test
	void postConEspaciosEnElTituloDevuelve400YNoGuarda() throws Exception {
		String json = """
				{
				  "titulo": " ",
				  "estado": "LEIDO",
				  "nota": 5
				}
				""";

		mockMvc.perform(post("/api/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value("Un libro no puede ir sin título"));

		verify(libroService, never()).guardar(any(Libro.class));
	}
}
