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

/*
Crea una clase Record TaskRequestDTO con los datos que envía el cliente al crear o actualizar una tarea: 
title (String), description (String), status (TaskStatus), priority (TaskPriority) y dueDate (LocalDate), sin id ni createdAt 
Añade javadoc en español a nivel de clase con un @param por cada componente — generado por IA.
*/
// Añade al final del record el campo categoryId de tipo Long, opcional (puede ser null), para asociar la tarea a una categoría — generado por IA
/* Añade al record TaskRequestDTO el componente tagIds (Set<Long>) al final, con los ids de las etiquetas que el cliente quiere asociar a la tarea.
Añade su @param en el javadoc y el import de java.util.Set. — generado por IA */
/* Aplica la validación custom @RequiereFechaLimite a nivel de clase — generado por IA */
/* Añade @Schema solo con description y example en español a los campos
   title, dueDate, categoryId y tagIds. En dueDate indica formato yyyy-MM-dd,
   que debe ser futura y que es obligatoria si la prioridad es HIGH. — generado por IA */
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
    
    /* Valida que title sea obligatorio, no esté en blanco y tenga máximo 150 caracteres, con mensajes de error en español — generado por IA */
    @Schema(description = "Título de la tarea", example = "Preparar la demo del sprint")
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los {max} caracteres")
    String title, 
    
    /* Valida que description, si se indica, no supere los 500 caracteres, con mensaje de error en español — generado por IA */
    @Size(max = 500, message = "La descripción no puede exceder los {max} caracteres")
    String description, 
    
    /* Valida que status sea obligatorio, con mensaje de error en español — generado por IA */
    @NotNull(message = "El estado es obligatorio")
    TaskStatus status, 
    
    /* Valida que priority sea obligatoria, con mensaje de error en español — generado por IA */
    @NotNull(message = "La prioridad es obligatoria")
    TaskPriority priority, 
    
    /* Valida que dueDate, si se indica, sea una fecha futura, con mensaje de error en español — generado por IA */
    @Schema(description = "Fecha límite en formato yyyy-MM-dd. Debe ser futura y es obligatoria si la prioridad es HIGH",
            example = "2030-12-31")
    @Future(message = "La fecha límite debe ser una fecha futura")
    LocalDate dueDate, 
    
    @Schema(description = "Identificador de una categoría existente", example = "1")
    Long categoryId, 
    
    @Schema(description = "Identificadores de etiquetas existentes", example = "[1, 2]")
    Set<Long> tagIds) {}


