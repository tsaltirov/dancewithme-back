# 💃 DanceWithMe — Backend

> API REST para la gestión integral de academias de baile: alumnos, grupos, eventos, vestuario y pagos.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/Auth-JWT-black?logo=jsonwebtokens)](https://jwt.io/)

---

## Índice

- [Tecnologías](#tecnologías)
- [Base de datos](#base-de-datos)
- [Autenticación](#autenticación)
- [Endpoints](#endpoints)
  - [Auth](#auth)
  - [Usuarios](#usuarios)
- [Variables de entorno](#variables-de-entorno)

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Framework | Spring Boot 4 + Spring Security 7 |
| Lenguaje | Java 21 |
| Base de datos | PostgreSQL 16 |
| ORM | Hibernate / Spring Data JPA |
| Autenticación | JWT (JJWT 0.12) + Refresh tokens |
| Almacenamiento de imágenes | Supabase Storage |
| Email | Spring Mail + SMTP |
| Documentación | SpringDoc OpenAPI 3 / Swagger UI |
| Build | Maven |

---

## Base de datos

El esquema es gestionado automáticamente por Hibernate (`ddl-auto=update` en desarrollo).

## Autenticación

Autenticación **stateless** con doble token:

| Token | Duración | Almacenamiento |
|---|---|---|
| Access token (JWT) | xx minutos | Cookie HTTP-Only o header `Authorization: Bearer` |
| Refresh token (UUID) | xx días | BD — revocable |

- **Web:** las cookies se gestionan automáticamente por el navegador
- **Mobile (Flutter):** tokens en el body de respuesta, enviar en header manual

Cada `/refresh` rota el refresh token. Un reseteo de contraseña revoca todas las sesiones activas.

---

## Endpoints

> Base URL: `http://localhost:3002/api/v1`
> Documentación interactiva: `http://localhost:3002/swagger-ui.html`

### Auth

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/auth/register` | Registro de usuario |
| `POST` | `/auth/login` | Inicio de sesión |
| `POST` | `/auth/refresh` | Renovar tokens |
| `POST` | `/auth/logout` | Cerrar sesión |
| `GET` | `/auth/verify?token=` | Verificar email |
| `POST` | `/auth/forgot-password` | Enviar código de reseteo |
| `POST` | `/auth/reset-password` | Resetear contraseña con código |

### Usuarios

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/users/{id}` | Obtener usuario por ID |
| `PATCH` | `/users/{id}/profile` | Actualizar nombre, apellido e imagen de perfil |

### Escuelas

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/schools/user/{userId}` | Escuelas de un usuario |
| `GET` | `/schools/{id}` | Detalle de una escuela |
| `POST` | `/schools` | Crear escuela |
| `PUT` | `/schools/{id}` | Actualizar escuela |

### Grupos y matrículas

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/groups/school/{schoolId}` | Grupos de una escuela |
| `GET` | `/groups/{id}` | Detalle de un grupo |
| `POST` | `/groups` | Crear grupo |
| `PUT` | `/groups/{id}` | Actualizar grupo |
| `DELETE` | `/groups/{id}` | Desactivar grupo (soft delete) |
| `GET` | `/enrollments/group/{groupId}` | Matrículas de un grupo |
| `GET` | `/enrollments/student/{studentId}` | Matrículas de un alumno |
| `POST` | `/enrollments` | Matricular alumno |
| `DELETE` | `/enrollments/{id}` | Cancelar matrícula |

### Eventos

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/events/school/{schoolId}` | Eventos de una escuela |
| `GET` | `/events/{id}` | Detalle de un evento |
| `POST` | `/events` | Crear evento |
| `PUT` | `/events/{id}` | Actualizar evento |
| `PATCH` | `/events/{id}/cancel` | Cancelar evento |
| `GET` | `/events/{eventId}/prices` | Precios del evento |
| `POST` | `/events/{eventId}/prices` | Añadir precio |
| `PUT` | `/events/{eventId}/prices/{priceId}` | Actualizar precio |
| `DELETE` | `/events/{eventId}/prices/{priceId}` | Eliminar precio |
| `GET` | `/events/{eventId}/participations` | Participaciones del evento |
| `POST` | `/events/participations` | Apuntar alumno a evento |
| `DELETE` | `/events/participations/{participationId}` | Desapuntar alumno de evento |

### Vestuario

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/costumes?schoolId=&active=` | Catálogo de prendas (`active` opcional: `true`/`false`/sin filtro) |
| `GET` | `/costumes/{id}` | Detalle de prenda |
| `POST` | `/costumes` | Crear prenda |
| `PUT` | `/costumes/{id}` | Actualizar prenda |
| `DELETE` | `/costumes/{id}` | Desactivar prenda (soft delete) |
| `PATCH` | `/costumes/{id}/activate` | Reactivar prenda archivada |
| `POST` | `/costumes/assign` | Asignar prenda a participación |
| `GET` | `/costumes/assignments/participation/{id}` | Asignaciones de una participación |
| `GET` | `/costumes/assignments/pending` | Asignaciones pendientes de devolución |
| `PATCH` | `/costumes/assignments/{id}/return` | Marcar prenda como devuelta |
| `DELETE` | `/costumes/assignments/{id}` | Eliminar asignación (solo si está en ENTREGADO) |

### Coreografías

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/coreografias/school/{schoolId}` | Coreografías de una escuela |
| `GET` | `/coreografias/{id}` | Detalle completo (bailarines, escenas, posiciones) |
| `POST` | `/coreografias` | Crear coreografía |
| `PUT` | `/coreografias/{id}` | Actualizar metadatos |
| `PUT` | `/coreografias/{id}/document` | Guardar documento completo (reemplaza todo) |
| `DELETE` | `/coreografias/{id}` | Eliminar coreografía |
| `GET` | `/coreografias/public/{schoolId}/{publicSlug}` | Vista pública (sin auth) |

## Variables de entorno

Crea un fichero `.env` en la raíz del proyecto:

# Swagger UI disponible en:
# http://localhost:3002/swagger-ui.html

