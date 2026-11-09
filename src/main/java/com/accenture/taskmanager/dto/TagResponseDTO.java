package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/*
Crea una clase record TagResponseDTO con los datos de una etiqueta que la API devuelve al cliente: id (Long) y name (String).
Añade javadoc en español a nivel de clase con un @param por cada componente. — generado por IA*/
/* Añade @Schema solo con description y example en español al campo id. — generado por IA */
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
