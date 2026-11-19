package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

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
