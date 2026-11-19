package com.accenture.taskmanager.validation;

import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.model.TaskPriority;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * RequiereFechaLimiteValidator es una clase que implementa la lógica de validación para la anotación personalizada @RequiereFechaLimite.
 * Esta clase verifica que las tareas con prioridad HIGH tengan una fecha límite establecida.
 */
public class RequiereFechaLimiteValidator implements ConstraintValidator<RequiereFechaLimite, TaskRequestDTO> {

    /**
     * Valida que las tareas con prioridad HIGH tengan una fecha límite establecida.
     *
     * @param taskRequestDTO El objeto TaskRequestDTO a validar.
     * @param context        El contexto de validación.
     * @return true si la validación es exitosa, false en caso contrario.
     */
    @Override
    public boolean isValid(TaskRequestDTO taskRequestDTO, ConstraintValidatorContext context) {
        if (taskRequestDTO == null || taskRequestDTO.priority() == null) {
            return true; // La validación de null se maneja con @NotNull
        }

        if (taskRequestDTO.priority() == TaskPriority.HIGH) {
            return taskRequestDTO.dueDate() != null;
        }

        return true;
    }
}
