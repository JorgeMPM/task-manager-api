package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/*Crea una clase record TagRequestDTO con los datos que envía el cliente al crear una etiqueta: name (String).
Añade javadoc en español a nivel de clase con un @param por cada componente. — generado por IA */
/* Añade @Schema solo con description y example en español al campo name. — generado por IA */

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
