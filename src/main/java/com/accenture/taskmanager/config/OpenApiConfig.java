package com.accenture.taskmanager.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/* Crea la clase OpenApiConfig con @Configuration y @OpenAPIDefinition:
   @Info con título, descripción, versión y contacto, y
   @Server de desarrollo en http://localhost:8080.
   Textos en español y Javadoc de clase en español. — generado por IA */
/**
 * Clase de configuración para OpenAPI.
 * Configura la documentación de la API utilizando anotaciones de OpenAPI.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Task Manager API",
        description = "API REST para la gestión de tareas, categorías y etiquetas",
        version = "1.0.0",
        contact = @Contact(
            name = "Jorge Martín",
            email = "jorgemartinpm@gmail.com"
        )
    ),
    servers = @Server(
        url = "http://localhost:8080",
        description = "Servidor de desarrollo"
    )
)
public class OpenApiConfig {
}   
