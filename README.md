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

## Variables de entorno

Crea un fichero `.env` en la raíz del proyecto:

# Swagger UI disponible en:
# http://localhost:3002/swagger-ui.html

