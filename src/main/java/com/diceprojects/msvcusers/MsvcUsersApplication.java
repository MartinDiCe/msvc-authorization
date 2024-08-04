package com.diceprojects.msvcusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal para iniciar la aplicación msvc-Users.
 * {@link SpringBootApplication} habilita la configuración automática de Spring Boot,
 * escaneo de componentes y otras configuraciones específicas de Spring.
 */
@SpringBootApplication
public class MsvcUsersApplication {

	/**
	 * Método principal para ejecutar la aplicación Spring Boot.
	 *
	 * @param args Argumentos de la línea de comandos pasados al iniciar la aplicación.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MsvcUsersApplication.class, args);
	}

}
