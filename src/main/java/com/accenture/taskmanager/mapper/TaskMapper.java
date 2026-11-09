package com.accenture.taskmanager.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.model.Task;

/**
 * Componente encargado de transformar la entidad {@code Task} en sus DTOs y viceversa.
 * Centraliza todas las conversiones para que ni el controller ni el service manipulen directamente los campos de los DTOs.
 */
@Component
public class TaskMapper {

    /*Añade un campo private final TagMapper tagMapper inyectado por constructor, para reutilizar la conversión de Tag a TagResponseDTO.
    Añade javadoc en español al constructor con @param. — generado por IA */
    private final TagMapper tagMapper;

    /**
     * Constructor de la clase {@code TaskMapper}.
     *
     * @param tagMapper El mapper de etiquetas utilizado para convertir entre entidades {@code Tag} y sus DTOs.
     */
    public TaskMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    /* Reescribe toResponseDTO para que, además de categoryName, rellene tags con una List<TagResponseDTO> convirtiendo cada Tag de la tarea con tagMapper.toResponseDTO.
    Si getTags() es null devuelve una lista vacía. Actualiza el javadoc en español con @param y @return. — generado por IA */
    /**
     * Transforma una entidad {@code Task} en un {@code TaskResponseDTO}, incluyendo el nombre de la categoría y las etiquetas asociadas.
     *
     * @param task La entidad {@code Task} a transformar.
     * @return Un {@code TaskResponseDTO} con los datos de la tarea, el nombre de la categoría si existe y una lista de etiquetas asociadas.
     */
    public TaskResponseDTO toResponseDTO(Task task) {
        String categoryName = (task.getCategory() != null) ? task.getCategory().getName() : null;
        List<TagResponseDTO> tagResponseDTOs = (task.getTags() != null) ? 
                task.getTags().stream()
                    .map(tagMapper::toResponseDTO)
                    .toList() : List.of();

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                categoryName,
                tagResponseDTOs
        );
    }

    /*
    Crea el método toEntity que reciba un TaskRequestDTO y devuelva una nueva entidad Task copiando title, description, status, priority y dueDate, sin asignar id ni createdAt
    Añade javadoc en español con @param y @return. — generado por IA
    */
    /**
     * Transforma un {@code TaskRequestDTO} en una nueva entidad {@code Task}.
     *
     * @param taskRequestDTO El DTO de solicitud de tarea a transformar.
     * @return Una nueva entidad {@code Task} con los datos del DTO.
     */
    public Task toEntity(TaskRequestDTO taskRequestDTO) {
        Task task = new Task();
        task.setTitle(taskRequestDTO.title());
        task.setDescription(taskRequestDTO.description());
        task.setStatus(taskRequestDTO.status());
        task.setPriority(taskRequestDTO.priority());
        task.setDueDate(taskRequestDTO.dueDate());
        return task;
    }
}