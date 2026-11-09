package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/*
 Crea el record LoginResponseDTO con el componente token (String), que contiene el JWT devuelto por POST /auth/login.
 Añade @Schema con description y un example que empiece por "eyJ". Añade Javadoc en español a nivel de clase con un @param por el componente,
 usando imports y no nombres de clase completos. — generado por IA
*/
/**
 * DTO de salida que representa la respuesta del servidor tras un inicio de sesión exitoso.
 *
 * @param token El token JWT devuelto por el servidor.
 */
public record LoginResponseDTO(
    
    @Schema(description = "Token JWT devuelto tras el inicio de sesión", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token) {
}