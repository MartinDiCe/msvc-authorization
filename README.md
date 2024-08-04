
# Microservicio Usuarios y Seguridad (msvc-users)

msvc-users es un microservicio diseñado para gestionar la autenticación y autorización de usuarios en múltiples microservicios. Facilita un control centralizado del acceso y asegura que solo los usuarios autorizados puedan acceder a los recursos y operaciones permitidas.

## Funcionalidades Principales

- **Autenticación y Emisión de Tokens:** Implementación de JWT para manejar la autenticación y la emisión de tokens seguros.
- **Gestión de Usuarios:** Creación, actualización y eliminación de usuarios, así como la gestión de sus estados y roles.
- **Autorización:** Verificación de permisos para acceder a determinados recursos o realizar ciertas acciones, basado en los roles de los usuarios.
- **Seguridad:** Implementación de medidas de seguridad como la encriptación de contraseñas, la validación de tokens y la gestión segura de sesiones.

## Tecnologías Utilizadas

- **Java** - Spring Boot
- **Spring WebFlux**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **MongoDB**
- **Lombok**
- **SpringDoc** - Para la documentación de la API

## Estructura del Proyecto

- **`src/main/java/com/tuorganizacion/msvcusers`**: Código fuente del microservicio.
    - `exceptions`: Clases relacionadas con el manejo de excepciones.
    - `context`: Contiene la clase para configurar Swagger.
    - `persistences`: Clases de persistencia, entidades y repositorios.
    - `services`: Lógica de negocio y servicios.
    - `controllers`: EndPoints de la API.
- **`src/test`**: Pruebas unitarias y de integración.

## Configuración

Asegúrate de configurar adecuadamente el archivo `application.properties` para la base de datos y otras configuraciones necesarias.

## Uso

1. **Compilación y Ejecución:** Utiliza Maven o tu herramienta de construcción preferida para compilar y ejecutar el proyecto.
   ```bash
   mvn clean install
   java -jar target/msvc-users.jar
   ```

## Endpoints

- `POST /auth/login`: Iniciar sesión y obtener un token JWT.
- `POST /users`: Crear un nuevo usuario.
- `GET /users/{id}`: Obtener los detalles de un usuario.
- `PUT /users/{id}`: Actualizar un usuario existente.
- `DELETE /users/{id}`: Eliminar un usuario.
- `GET /roles`: Obtener todos los roles.
- `POST /roles`: Crear un nuevo rol.
- `PUT /roles/{id}`: Actualizar un rol existente.
- `DELETE /roles/{id}`: Eliminar un rol.

Documentación detallada de la API disponible en: `[HOST]:[PORT]/doc/swagger-ui/index.html`

## Contribuir

¡Contribuciones son bienvenidas! Si encuentras errores o mejoras, abre un problema o envía una solicitud de extracción.

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

Estas dependencias están definidas en el archivo `pom.xml` del proyecto.
