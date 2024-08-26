# Microservicio de Autorización (msvc-authorization)

`msvc-authorization` es un microservicio diseñado para gestionar la autorización de usuarios en múltiples microservicios. Facilita un control centralizado de roles y permisos, asegurando que solo los usuarios autorizados puedan acceder a los recursos y realizar las operaciones permitidas. Este microservicio se comunica con `msvc-authentication` para manejar la seguridad, con `msvc-configurations` para obtener parametros y configuraciones del sistema y con `msvc-Gateway` para controlar la seguridad y la comunicación entre microservicios.

## Funcionalidades Principales

- **Gestión de Roles:** Creación, asignación y eliminación de roles a usuarios, con la capacidad de cambiar el estado de un rol.
- **Gestión de Usuarios:** Creación, asignación y eliminación de usuarios, con la capacidad de cambiar el estado de un Usuario.
- **Autorización:** Verificación de permisos para acceder a determinados recursos o realizar ciertas acciones, basado en los roles de los usuarios.
- **Seguridad:** Integración con `msvc-authentication` para la validación de tokens y la autenticación de usuarios.
- **Configuración Centralizada:** Integración con un servicio de configuración para manejar estados de entidades y otros parámetros clave con `msvc-configurations`.
- **Comunicación con API Gateway:** Gestión de seguridad y rutas a través del `msvc-Gateway` para asegurar y controlar el acceso a los microservicios.

## Tecnologías Utilizadas

- **Java** - Spring Boot
- **Spring WebFlux** - Para construir aplicaciones no bloqueantes y reactivas.
- **Spring Security** - Para manejar las contraseñas, la  seguridad y la autorización, en coordinación con `msvc-authentication`.
- **JWT (JSON Web Tokens)** - Validación de tokens en el proceso de autorización.
- **MongoDB** - Base de datos no relacional para la gestión de usuarios y roles.
- **Lombok** - Para reducir el código boilerplate.
- **SpringDoc** - Para la documentación de la API.

## Estructura del Proyecto

- **`src/main/java/com/tuorganizacion/msvcauthorization`**: Código fuente del microservicio.
  - **`clients`**: Clases para la comunicación con otros microservicios, como `msvc-authentication` y el servicio de configuración.
  - **`exceptions`**: Clases relacionadas con el manejo de excepciones.
  - **`config`**: Clases de configuración de seguridad y otros aspectos del microservicio.
  - **`persistences`**: Clases de persistencia, entidades, DTOs y repositorios.
  - **`services`**: Lógica de negocio, incluyendo la gestión de roles y estados de entidades.
  - **`controllers`**: Endpoints de la API.
  - **`utils`**: Clases utilitarias, como el manejo del estado de entidades a través de la configuración.
  - **`initialization`**: Clases para la inicialización de datos predeterminados al inicio de la aplicación.
- **`src/test`**: Pruebas unitarias y de integración.

## Configuración

Asegúrate de configurar adecuadamente el archivo `application-dev.properties` y `application-prod.properties` para la base de datos MongoDB y otras configuraciones necesarias, como las URLs de `msvc-authentication` y `API Gateway`.

### Ejemplo de Configuración (`application.properties`):

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/msvc-authorization
msvc.authentication.url=http://localhost:8002/api/
msvc.configuration.url=http://localhost:8005/api/
msvc.gateway.url=http://localhost:8001/api/
```

## Uso

1. **Compilación y Ejecución:** Utiliza Maven o tu herramienta de construcción preferida para compilar y ejecutar el proyecto.
   ```bash
   mvn clean install
   java -jar target/msvc-authorization.jar
   ```

2. **Inicialización de Datos:** Al iniciar la aplicación, ciertos parámetros clave, como "EntityStatus", se inicializan automáticamente si no existen en la base de datos.

## Endpoints

- **Roles:**
  - `GET /roles`: Obtener todos los roles.
  - `POST /roles`: Crear un nuevo rol.
  - `PUT /roles/{id}`: Actualizar un rol existente.
  - `DELETE /roles/{id}`: Eliminar un rol.
  - `PUT /roles/{id}/status`: Cambiar el estado de un rol.

- **Usuarios:**
  - `GET /api/user/{username}`: Obtener los detalles de un usuario por su nombre de usuario.
  - `POST /api/user/create`: Crear un nuevo usuario con el nombre de usuario, contraseña y roles especificados.
  - `PUT /api/user/updateToken/{userId}`: Actualizar el token de seguridad de un usuario.
  - `GET /api/user/findById/{userId}`: Obtener los detalles de un usuario por su ID.

- **Autorización:**
  - `GET /users/{id}/permissions`: Obtener los permisos de un usuario en función de sus roles. (Pendiente)

Documentación detallada de la API disponible en: `[DOMINIO]:[PORT]/apidoc/webjars/swagger-ui/index.html`

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
