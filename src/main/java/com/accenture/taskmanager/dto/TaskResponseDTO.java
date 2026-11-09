package com.accenture.taskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

/*
Crea una clase record TaskResponseDTO con los datos que la API devuelve al cliente: id (Long), title (String), description (String), status (TaskStatus), priority (TaskPriority), dueDate (LocalDate) y createdAt (LocalDateTime)
Añade javadoc en español a nivel de clase con un @param por cada componente. — generado por IA.
*/
// Añade al final del record el campo categoryName de tipo String con el nombre de la categoría, null si la tarea no tiene categoría — generado por IA
/* Añade al record TaskResponseDTO el componente tags ({@code List<TagResponseDTO>}) al final, con las etiquetas asociadas a la tarea.
Añade su @param en el javadoc y el import de java.util.List. — generado por IA */
/* Añade @Schema solo con description y example en español a los campos id, createdAt, categoryName y tags. — generado por IA */
/**
 * DTO de salida que representa la respuesta de la API con los datos de una tarea.
 *
 * @param id          El identificador único de la tarea.
 * @param title       El título de la tarea.
 * @param description La descripción de la tarea.
 * @param status      El estado de la tarea (TaskStatus).
 * @param priority    La prioridad de la tarea (TaskPriority).
 * @param dueDate     La fecha límite para completar la tarea (LocalDate).
 * @param createdAt   La fecha y hora en que se creó la tarea (LocalDateTime).
 * @param categoryName El nombre de la categoría a la que pertenece la tarea (null si no tiene categoría).
 * @param tags        La lista de etiquetas asociadas a la tarea ({@code List<TagResponseDTO>}).
 */
public record TaskResponseDTO(
    
    @Schema(description = "Identificador de la tarea, generado por el servidor", example = "1")
    Long id, 
    
    String title, 
    
    String description, 
    
    TaskStatus status, 
    
    TaskPriority priority, 
    
    LocalDate dueDate, 
    
    @Schema(description = "Fecha y hora de creación, asignada por el servidor", example = "2023-01-01T12:00:00")
    LocalDateTime createdAt, 
    
    @Schema(description = "El nombre de la categoría a la que pertenece la tarea (null si no tiene categoría)", example = "Backend")
    String categoryName, 
    
    @Schema(description = "La lista de etiquetas asociadas a la tarea")
    List<TagResponseDTO> tags) {
}
