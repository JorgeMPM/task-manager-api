package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/*
 Crea el record LoginRequestDTO con los datos que envía el cliente en POST /auth/login: username (String) y password (String).
 Valida que ambos sean obligatorios y no estén en blanco con @NotBlank y mensajes de error en español.
 Añade @Schema con description y example solo en username (example "admin"); en password añade solo description, sin example.
 Sobrescribe toString() para que no muestre la contraseña (por ejemplo, password=****).
 Añade Javadoc en español a nivel de clase con un @param por cada componente, y Javadoc en toString(). — generado por IA
*/
/**
 * DTO de entrada que representa la solicitud de inicio de sesión del cliente.
 * 
 * @param username El nombre de usuario del cliente.
 * @param password La contraseña del cliente.
 */
public record LoginRequestDTO(
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario", example = "admin")
    String username,

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario")
    String password) {

    /**
     * Devuelve una representación en cadena del objeto, ocultando la contraseña.
     *
     * @return Una cadena que representa el objeto, con la contraseña oculta.
     */
    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "username='" + username + '\'' +
                ", password='****'" +
                '}';
    }
}
