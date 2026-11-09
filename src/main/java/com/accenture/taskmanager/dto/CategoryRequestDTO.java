package com.accenture.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/*Crea una clase record CategoryRequestDTO con los datos que envía el cliente al crear o actualizar una categoría: name (String) y description (String), sin id ni tasks
Añade javadoc en español a nivel de clase con un @param por cada componente. — generado por IA */
/* Añade @Schema solo con description y example en español a los campos
   name y description. — generado por IA */
/**
 * Clase que representa la solicitud de creación o actualización de una categoría en el sistema de gestión de tareas.
 * Esta clase es un record que contiene los datos enviados por el cliente para crear o actualizar una
 * categoría.
 * 
 * @param name el nombre de la categoría.
 * @param description la descripción de la categoría.
 */
public record CategoryRequestDTO(
    
    @Schema(description = "El nombre de la categoría", example = "Backend")
    String name, 
    
    @Schema(description = "La descripción de la categoría", example = "Categoría para tareas relacionadas con el desarrollo backend")
    String description) {
}

