package com.accenture.taskmanager.dto;

import java.time.LocalDate;
import java.util.Set;

import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;
import com.accenture.taskmanager.validation.RequiereFechaLimite;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** 
 * DTO de entrada que representa la solicitud de creación o actualización de una tarea.
 * 
 * @param title       El título de la tarea.
 * @param description La descripción de la tarea.
 * @param status      El estado de la tarea (TaskStatus).
 * @param priority    La prioridad de la tarea (TaskPriority).
 * @param dueDate     La fecha límite para completar la tarea (LocalDate).
 * @param categoryId  El identificador de la categoría a la que pertenece la tarea (opcional).
 * @param tagIds      Un conjunto de identificadores de etiquetas asociadas a la tarea (opcional).
 */
@RequiereFechaLimite
public record TaskRequestDTO(
    
    @Schema(description = "Título de la tarea", example = "Preparar la demo del sprint")
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los {max} caracteres")
    String title, 
    
    @Size(max = 500, message = "La descripción no puede exceder los {max} caracteres")
    String description, 
    
    @NotNull(message = "El estado es obligatorio")
    TaskStatus status, 
    
    @NotNull(message = "La prioridad es obligatoria")
    TaskPriority priority, 
    
    @Schema(description = "Fecha límite en formato yyyy-MM-dd. Debe ser futura y es obligatoria si la prioridad es HIGH",
            example = "2030-12-31")
    @Future(message = "La fecha límite debe ser una fecha futura")
    LocalDate dueDate, 
    
    @Schema(description = "Identificador de una categoría existente", example = "1")
    Long categoryId, 
    
    @Schema(description = "Identificadores de etiquetas existentes", example = "[1, 2]")
    Set<Long> tagIds) {}


