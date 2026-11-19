package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Clase que representa la solicitud de creación o actualización de una etiqueta en el sistema de gestión de tareas.
 * Esta clase es un record que contiene los datos enviados por el cliente para crear o actualizar una
 * etiqueta.
 * 
 * @param name el nombre de la etiqueta.
 */
public record TagRequestDTO(
    
    @Schema(description = "El nombre de la etiqueta", example = "urgente")
    String name) {

}
