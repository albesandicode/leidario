## Fecha: 12/11/2026 12:27

## 1. Arquitectura: opción B

Decisión: Opción B: parte visual (Angular) + servidor propio (Spring Boot) + base de datos (PostgreSQL).

Opciones: A (solo parte visual, con los libros guardados en el navegador) y B (parte visual + servidor + base de datos). Descarto la A.

Por qué: La A no cumple nada nuevo: sería otra app de front, como Prevlio. Quiero al menos una buena aplicación full stack, y para eso Leidario tiene que guardar los libros en mi propio servidor y base de datos.

Riesgo o pendiente: Hay más piezas que montar y conectar. Por eso el tracker ocupa este sprint y el siguiente, en vez de una semana. El objetivo de este sprint es solo el primer libro guardado en PostgreSQL.

## 2. Estructura del repositorio

Decisión: Un solo repositorio con dos carpetas: `frontend` (Angular) y `backend` (Spring Boot).

Opciones: Un repo con las dos partes, o dos repos separados (como en ModNestMC). Descarto los dos repos por ahora.

Por qué: Es una app pequeña de un solo desarrollador. Con un repo, un ticket que toca las dos partes va en un solo PR, y el brief, el CLAUDE.md y este archivo están en un único sitio.

Riesgo o pendiente: El CI tiene que saber qué parte ha cambiado para no compilar las dos en cada push. Lo resuelvo con dos workflows, uno por carpeta.

## 3. Base de datos en local

Decisión: PostgreSQL en Docker durante el desarrollo.

Opciones: Instalar PostgreSQL en el PC, Docker o una base de datos en la nube desde el primer día. Descarto la instalación directa y la nube por ahora.

Por qué: Ya usé Docker en la Formación Dual. Con un archivo de configuración arranco y paro la base de datos cuando quiero, sin instalar nada fijo en el PC. Para el objetivo de este sprint, el primer libro guardado, no necesito nada en la nube.

Riesgo o pendiente: Para la prueba del reinicio, los datos tienen que guardarse en un volumen. Si no, al borrar el contenedor se pierden y la prueba falla aunque el código esté bien.

## 4. Cómo habla Spring Boot con la base de datos

Decisión: Spring Data JPA.

Opciones: Spring Data JPA, JdbcTemplate (SQL escrito a mano) y Spring Data JDBC. Descarto JdbcTemplate y JDBC.

Por qué: JPA es lo más habitual en las ofertas de Spring. Para un CRUD de libros me ahorra escribir el SQL básico y puedo centrarme en entender las capas: controlador, servicio y repositorio.

Riesgo o pendiente: JPA esconde el SQL que ejecuta. Activaré que lo muestre en la consola para ver qué hace por debajo y no programar a ciegas.

## 5. Versión de Java y gestor de proyecto

Decisión: Maven y la versión LTS de Java que proponga Spring Initializr.

Opciones: Maven o Gradle. Descarto Gradle.

Por qué: Maven es lo que vi en las prácticas y sigue siendo lo más común en ofertas de Java en España. Elegir una versión LTS me asegura soporte largo y compatibilidad con Spring Boot.

Riesgo o pendiente: Tengo que comprobar en start.spring.io qué versiones de Java y de Spring Boot salen hoy, y que el CI use la misma versión de Java que mi PC.

## 6. Tests del backend

Decisión: Tests de integración con JUnit y MockMvc, contra un PostgreSQL de verdad levantado con Testcontainers.

Opciones: Testcontainers (PostgreSQL real en Docker), H2 (base de datos en memoria) o solo pruebas manuales con Postman. Descarto H2 y las pruebas solo manuales.

Por qué: H2 no se comporta exactamente igual que PostgreSQL, y un test que pasa en H2 puede fallar en producción. Como ya uso Docker en local, Testcontainers no añade herramientas nuevas, y GitHub Actions trae Docker.

Riesgo o pendiente: Los tests serán más lentos que con H2. Si la configuración de Testcontainers se come el día del APP3-001, dejo los tests automáticos para el día siguiente y lo apunto aquí.

## 7. Estilos CSS

Decisión: CSS, igual que en Prevlio.

Opciones: CSS, Sass/SCSS y Tailwind CSS. Descarto SCSS y Tailwind por ahora.

Por qué: Lo nuevo de esta app es el backend. Prefiero no sumar otra tecnología en la parte visual. Reutilizo la organización de variables CSS de Prevlio para colores y espacios.

Riesgo o pendiente: Los tonos cálidos de papel del brief los defino como variables desde el principio para no repetir valores.

## 8. Renderizado: sin SSR ni SSG

Decisión: Renderizado en el navegador, sin SSR ni SSG.

Opciones: Renderizado en el cliente, SSR y SSG. Descarto SSR y SSG.

Por qué: Leidario es una herramienta personal, no una web de contenido que tenga que salir en Google. Los libros llegan desde mi servidor después de cargar la página, así que prerenderizarla no aporta nada.

Riesgo o pendiente: Sin JavaScript la app no funciona. Además, a diferencia de Prevlio, sin conexión con el servidor no hay libros. Tendré que mostrar un aviso claro cuando el servidor no responda.

## 9. Integración de IA en Angular

Decisión: None, igual que en Prevlio.

Por qué: Sigo con mi propio CLAUDE.md. Ahora tendrá que cubrir las dos partes: el stack, cómo arrancar cada una y las comprobaciones.

Riesgo o pendiente: El CLAUDE.md tiene que dejar claro qué reglas valen para el frontend, cuáles para el backend y cuáles para los dos.

## 10. Hosting (pendiente, siguiente sprint)

Decisión: Este sprint todo funciona en local. El hosting lo decido en el siguiente sprint.

Por qué: El objetivo de este sprint es el primer libro guardado en PostgreSQL, y eso no necesita publicarse. Elegir ahora dónde alojar el servidor y la base de datos me frenaría sin aportar nada al objetivo.

## 11. Partir el APP3-001

Decisión: El APP3-001 queda en guardar, recuperar y que el libro siga ahí tras reiniciar el servidor. La validación, los errores y el orden pasan al APP3-002.

Por qué: Montar el proyecto y conectarlo a la base de datos ya llena el día, y prefiero cerrar algo que funcione y esté probado antes que dejar dos cosas a medias.

Riesgo o pendiente:
- A diferencia de Prevlio, aquí hay que alojar tres cosas: el frontend (puede seguir en GitHub Pages), el servidor de Spring Boot y la base de datos. Las dos últimas no caben en GitHub Pages.
- Antes de decidir tengo que revisar qué servicios tienen plan gratuito hoy y sus límites: si se duermen cuando nadie los usa, si la base de datos caduca, etc.
- Si el frontend y el servidor están en dominios distintos, tengo que configurar CORS en Spring Boot.
- Sin inicio de sesión, cualquiera con el enlace podría modificar los libros. Tengo que elegir entre una versión de demostración, una contraseña sencilla o publicarla más adelante.

## 12. Guardado como texto en la base de datos

Con enum, un libro solo puede tener uno de los tres estados definidos (QUIERO_LEER, LEYENDO o LEIDO), mientras que con String podría guardarse cualquier valor, como HOLA.

## 13. Quitar la comprobación extra

Decisión: La CI espera a PostgreSQL solo con el health check del servicio.

Por qué: GitHub no arranca los pasos hasta que el servicio está sano, así que el paso extra hacía lo mismo. Son menos líneas que mantener y defender.

## 14. La primera entrega sale como está y el cambio de carpeta va aparte

Fecha: 19-11 · Cuándo se hace: al principio del sprint 6, antes de la parte visual.

- Qué decidimos: envío la primera entrega a Alex tal como está, y paso el proyecto a su carpeta en una entrega aparte.
- Alternativas que miramos: cambiar la carpeta dentro de la primera entrega, o no cambiarla.
- Por qué esta: una entrega pequeña, y la estructura más ordenada, como en los otros proyectos.
- Qué perdemos con ella: una revisión más, y la primera entrega llega con la carpeta todavía sin ordenar.
- Cuándo la cambiaríamos: si Alex pide ver la estructura definitiva ya en la primera entrega.

## 15. Docker ahora: la base de datos se arranca con un solo paso

Fecha: 19-11 · Cuándo se hace: al principio del sprint 6, antes de la parte visual.

- Qué decidimos: quien descargue el proyecto arranca la base de datos con un solo paso, sin instalar nada más.
- Alternativas que miramos: instalar la base de datos en el ordenador, o tenerla en internet.
- Por qué esta: ya he visto Docker y lo piden muchas ofertas.
- Qué perdemos con ella: hay que tener Docker instalado y abierto, y gasta memoria del ordenador.
- Cuándo la cambiaríamos: si Docker diera problemas que me impidieran trabajar.

## Pendiente de decidir (siguiente sprint, al diseñar la pantalla)

- Navegación: ¿una sola pantalla con la cuadrícula y los filtros, o una pantalla aparte para añadir o editar un libro?
- Añadir libro: ¿botón en el centro con la colección vacía y un + arriba cuando ya hay libros, como en Prevlio?
- Avisos: ¿mismo sistema que Prevlio (debajo del encabezado, con × de 44 px)?
- Diseño: boceto, variantes de la IA y elección con su porqué.
