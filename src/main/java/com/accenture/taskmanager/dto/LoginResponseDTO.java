package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de salida que representa la respuesta del servidor tras un inicio de sesión exitoso.
 *
 * @param token El token JWT devuelto por el servidor.
 */
public record LoginResponseDTO(
    
    @Schema(description = "Token JWT devuelto tras el inicio de sesión", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token) {
}