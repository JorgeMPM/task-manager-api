package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Clase que representa la respuesta de una etiqueta en el sistema de gestión de tareas.
 * Esta clase es un record que contiene los datos de una etiqueta que la API devuelve al cliente.
 * 
 * @param id el identificador de la etiqueta.
 * @param name el nombre de la etiqueta.
 */
public record TagResponseDTO(
    
    @Schema(description = "Identificador de la etiqueta, generado por el servidor", example = "1")
    Long id, 
    
    String name) {
}
