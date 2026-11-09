package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/*
Crea una clase record CategoryResponseDTO con los datos de una categoría que la API devuelve al cliente: id (Long), name (String) y description (String), sin la colección tasks
Añade javadoc en español a nivel de clase con un @param por cada componente. — generado por IA*/
/* Añade @Schema solo con description y example en español al campo id. — generado por IA */
/**
 * Clase que representa la respuesta de una categoría en el sistema de gestión de tareas.
 * Esta clase es un record que contiene los datos de una categoría que la API devuelve al cliente.
 * 
 * @param id el identificador de la categoría.
 * @param name el nombre de la categoría.
 * @param description la descripción de la categoría.
 */
public record CategoryResponseDTO(
    
    @Schema(description = "Identificador de la categoría, generado por el servidor", example = "1")
    Long id, 
    
    String name, 
    
    String description) {


}
