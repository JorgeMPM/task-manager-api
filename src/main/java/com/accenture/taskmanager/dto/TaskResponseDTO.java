package com.accenture.taskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

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
