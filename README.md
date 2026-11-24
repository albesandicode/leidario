# Leidario

Registro de libros leídos. Backend con Spring Boot y PostgreSQL en Docker.

## Arrancar en local (PowerShell)

Requisitos: Docker Desktop en marcha y Java 21.

```powershell
$env:DB_PASSWORD = "elige_una_contraseña"   # la usan Docker y Spring
docker compose up -d                        # PostgreSQL 16 en el puerto 5433
cd backend
.\mvnw spring-boot:run                      # API en http://localhost:8080/api/libros
```