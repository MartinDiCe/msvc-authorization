# Microservicio de Autorización (msvc-authorization)

`msvc-authorization` es un microservicio diseñado para gestionar la autorización de usuarios en múltiples microservicios. Facilita un control centralizado de roles y permisos, asegurando que solo los usuarios autorizados puedan acceder a los recursos y realizar las operaciones permitidas.

## Funcionalidades Principales

- **Gestión de Usuarios:** Creación, actualización y eliminación de usuarios, así como la gestión de sus estados.
- **Gestión de Roles:** Creación, asignación y eliminación de roles a usuarios.
- **Autorización:** Verificación de permisos para acceder a determinados recursos o realizar ciertas acciones, basado en los roles de los usuarios.
- **Seguridad:** Implementación de medidas de seguridad como la encriptación de contraseñas y la validación de tokens para autorización.

## Tecnologías Utilizadas

- **Java** - Spring Boot
- **Spring WebFlux**
- **Spring Security**
- **JWT (JSON Web Tokens)** - Para la validación de tokens en el proceso de autorización.
- **MongoDB** - Base de datos no relacional para la gestión de usuarios y roles.
- **Lombok** - Para reducir el código boilerplate.
- **SpringDoc** - Para la documentación de la API.

## Estructura del Proyecto

- **`src/main/java/com/tuorganizacion/msvcauthorization`**: Código fuente del microservicio.
  - **`exceptions`**: Clases relacionadas con el manejo de excepciones.
  - **`config`**: Clases de configuración de seguridad y otros aspectos del microservicio.
  - **`persistences`**: Clases de persistencia, entidades y repositorios.
  - **`services`**: Lógica de negocio y servicios.
  - **`controllers`**: EndPoints de la API.
- **`src/test`**: Pruebas unitarias y de integración.

## Configuración

Asegúrate de configurar adecuadamente el archivo `application.properties` para la base de datos MongoDB y otras configuraciones necesarias.

## Uso

1. **Compilación y Ejecución:** Utiliza Maven o tu herramienta de construcción preferida para compilar y ejecutar el proyecto.
   ```bash
   mvn clean install
   java -jar target/msvc-authorization.jar
   ```

## Endpoints

- **Usuarios:**
  - `POST /users`: Crear un nuevo usuario.
  - `GET /users/{id}`: Obtener los detalles de un usuario.
  - `PUT /users/{id}`: Actualizar un usuario existente.
  - `DELETE /users/{id}`: Eliminar un usuario.

- **Roles:**
  - `GET /roles`: Obtener todos los roles.
  - `POST /roles`: Crear un nuevo rol.
  - `PUT /roles/{id}`: Actualizar un rol existente.
  - `DELETE /roles/{id}`: Eliminar un rol.

- **Autorización:**
  - `GET /users/{id}/permissions`: Obtener los permisos de un usuario en función de sus roles.

Documentación detallada de la API disponible en: `[HOST]:[PORT]/apidoc/webjars/swagger-ui/index.html`

## Contribuir

¡Contribuciones son bienvenidas! Si encuentras errores o tienes sugerencias de mejora, abre un problema o envía una solicitud de extracción.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE.md` para más detalles.

## Dependencias

Este proyecto utiliza las siguientes dependencias principales:

- **Spring Boot Starter Data MongoDB Reactive**
- **Spring Boot Starter Security**
- **Spring Boot Starter WebFlux**
- **JWT (JSON Web Tokens)**
- **Lombok**
- **SpringDoc OpenAPI WebFlux UI**

Las dependencias están definidas en el archivo `pom.xml` del proyecto.
