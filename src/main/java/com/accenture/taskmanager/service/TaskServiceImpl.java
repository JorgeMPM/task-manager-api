package com.accenture.taskmanager.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.exception.TaskNotFoundException;
import com.accenture.taskmanager.mapper.TaskMapper;
import com.accenture.taskmanager.model.Tag;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;
import com.accenture.taskmanager.repository.CategoryRepository;
import com.accenture.taskmanager.repository.TagRepository;
import com.accenture.taskmanager.repository.TaskRepository;

/*
Crea una clase TaskServiceImpl que implemente TaskService y esté anotada con @Service.
Debe tener un atributo privado final con la lista de tareas en memoria, con las mismas dos tareas iniciales que tiene ahora TaskController.
Añade Javadoc a la clase. No uses clases de HTTP ni Optional.
— generado por IA.
*/

/**
 * Implementación de la interfaz TaskService que gestiona las operaciones de negocio para las tareas.
 * Delega el acceso a datos en {@link TaskRepository} y utiliza {@code TaskMapper}
 * para convertir entre la entidad {@code Task} y sus DTOs.
 * 
 * @author jorge martin perez moreno
 * @version 1.0
 */
@Service
public class TaskServiceImpl  implements TaskService {

    /*
    Añade un campo private final CategoryRepository categoryRepository
    e inclúyelo como parámetro y asignación en el constructor existente, sin quitar los campos ni parámetros actuales.
    Actualiza el Javadoc del constructor con su param. — generado por IA
    */
    private final CategoryRepository categoryRepository;

    // Repositorio de Spring Data JPA para acceder a las tareas en la base de datos.
    private final TaskRepository taskRepository;

    // Mapper para convertir entre la entidad Task y sus DTOs.
    private final TaskMapper taskMapper;

    // Añade el campo private final TagRepository tagRepository.
    private final TagRepository tagRepository;

    /**
     * Constructor de TaskServiceImpl que inyecta el repositorio y el mapper.
     * @param taskRepository el repositorio de tareas a inyectar
     * @param taskMapper el mapper de tareas a inyectar
     * @param categoryRepository el repositorio de categorías a inyectar
     * @param tagRepository el repositorio de etiquetas a inyectar
     */
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    // Reescribe findAll para que obtenga el Page<Task> con taskRepository.findAll, convierta su contenido en TaskResponseDTO con stream y taskMapper::toResponseDTO, y devuelva un PagedResponseDTO con content, number, size, totalElements, totalPages y isLast del Page, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public PagedResponseDTO<TaskResponseDTO> findAll(Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAll(pageable);
        List<TaskResponseDTO> content = taskPage.getContent()
                                                .stream()
                                                .map(taskMapper::toResponseDTO)
                                                .toList();
        return new PagedResponseDTO<>(content, taskPage.getNumber(), taskPage.getSize(),
                taskPage.getTotalElements(), taskPage.getTotalPages(), taskPage.isLast());
    }
    
    // Reescribe findById para que busque la tarea con taskRepository.findById, lance la misma ResponseStatusException NOT_FOUND si no existe y devuelva un TaskResponseDTO usando taskMapper.toResponseDTO, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResponseDTO findById(Long id) {
        
        /* Busca la tarea con taskRepository.findById y, si no existe, lanza new TaskNotFoundException(id) con orElseThrow — generado por IA */
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponseDTO(task);
    }

    /* Reescribe create para que, además de la categoría, resuelva los tagIds del DTO: si no son null ni vacíos, obtiene las etiquetas con tagRepository.findAllById
    si falta alguna, calcula los ids no encontrados y lanza ResponseStatusException con HttpStatus.NOT_FOUND y el mensaje "Etiquetas no encontradas con id: " seguido de esos ids
    si están todas, las asigna a la tarea con setTags en un nuevo HashSet. Mantén la lógica actual de la categoría y el Javadoc {@inheritDoc}. — generado por IA */
    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TaskResponseDTO create(TaskRequestDTO taskRequestDTO) {
        Task task = taskMapper.toEntity(taskRequestDTO);
        if (taskRequestDTO.categoryId() != null) {
        
            /* Asigna la categoría con categoryRepository.findById y, si no existe, lanza
               new IllegalArgumentException("Categoría con id " + id + " no encontrada")
               con orElseThrow — generado por IA */
            task.setCategory(categoryRepository.findById(taskRequestDTO.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría con id " + taskRequestDTO.categoryId() + " no encontrada")));
        }
        if (taskRequestDTO.tagIds() != null && !taskRequestDTO.tagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(taskRequestDTO.tagIds());
            if (tags.size() != taskRequestDTO.tagIds().size()) {
                List<Long> foundTagIds = tags.stream().map(Tag::getId).toList();
                List<Long> missingTagIds = taskRequestDTO.tagIds().stream()
                        .filter(id -> !foundTagIds.contains(id))
                        .toList();
                
                /* Lanza new IllegalArgumentException con el mensaje
                   "Tag con id " + missingTagIds + " no encontrado" — generado por IA */
                throw new IllegalArgumentException("Tag con id " + missingTagIds + " no encontrado");
            }
            task.setTags(new HashSet<>(tags));
        }
        return taskMapper.toResponseDTO(taskRepository.save(task));
    }

    /* Reescribe update para que, además de la categoría, resuelva los tagIds del DTO con tagRepository.findAllById, con la misma validación y el mismo mensaje de error que create.
    Si tagIds es null o vacío, la tarea queda sin etiquetas. No uses setTags: modifica la colección existente con getTags().clear() y getTags().addAll(...) después de validar.
    Mantén el resto de la lógica actual, el return null si la tarea no existe y el Javadoc {@inheritDoc}. — generado por IA */
    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        
        /* Busca la tarea con taskRepository.findById y, si no existe, lanza new TaskNotFoundException(id) con orElseThrow — generado por IA */
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    
            existingTask.setTitle(dto.title());
            existingTask.setDescription(dto.description());
            existingTask.setDueDate(dto.dueDate());
            existingTask.setPriority(dto.priority());
            existingTask.setStatus(dto.status());

        if (dto.categoryId() == null) {
            existingTask.setCategory(null);
        } else {
        
            /* Asigna la categoría con categoryRepository.findById y, si no existe, lanza
               new IllegalArgumentException("Categoría con id " + id + " no encontrada")
               con orElseThrow — generado por IA */
            existingTask.setCategory(categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría con id " + dto.categoryId() + " no encontrada")));
        }

        if (dto.tagIds() == null || dto.tagIds().isEmpty()) {
            existingTask.getTags().clear();
        } else {
            List<Tag> tags = tagRepository.findAllById(dto.tagIds());
            if (tags.size() != dto.tagIds().size()) {
                List<Long> foundTagIds = tags.stream().map(Tag::getId).toList();
                List<Long> missingTagIds = dto.tagIds().stream()
                        .filter(tagId -> !foundTagIds.contains(tagId))
                        .toList();
                
                /* Lanza new IllegalArgumentException con el mensaje
                   "Tag con id " + missingTagIds + " no encontrado" — generado por IA */
                throw new IllegalArgumentException("Tag con id " + missingTagIds + " no encontrado");
            }
            existingTask.getTags().clear();
            existingTask.getTags().addAll(tags);
        }

        return taskMapper.toResponseDTO(taskRepository.save(existingTask));
    }

    //Añade la anotación @Transactional encima de este método. No modifiques la firma ni la lógica existente.— generado por IA
    /*
    Reescribe el método delete(int id) para eliminar la tarea mediante taskRepository, sin utilizar la lista en memoria.
    Comprueba si existe la tarea utilizando el Repository.
    Si existe, elimínala y devuelve true.
    Si no existe, devuelve false.
    Mantén la firma del método definida en TaskService.
    — generado por IA
    */
    /**
    * {@inheritDoc}
    */
    @Transactional
    @Override
    public void delete(Long id) {   
        /* Si la tarea no existe según taskRepository.existsById, lanza
           new TaskNotFoundException(id); si existe, la elimina con deleteById — generado por IA */
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    // Reescribe findByStatus para que obtenga la lista con taskRepository.findByStatus y convierta cada Task en TaskResponseDTO usando stream, map con taskMapper::toResponseDTO y toList, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskResponseDTO> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                            .stream()
                            .map(taskMapper::toResponseDTO)
                            .toList();
    }

    // Reescribe findByPriority para que obtenga la lista con taskRepository.findByPriority y convierta cada Task en TaskResponseDTO usando stream, map con taskMapper::toResponseDTO y toList, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskResponseDTO> findByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority)
                            .stream()
                            .map(taskMapper::toResponseDTO)
                            .toList();
    }

    // Reescribe findByCategoryId para que obtenga el Page<Task> con taskRepository.findByCategoryId, convierta su contenido en TaskResponseDTO con stream y taskMapper::toResponseDTO, y devuelva un PagedResponseDTO con content, number, size, totalElements, totalPages y isLast del Page, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public PagedResponseDTO<TaskResponseDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        Page<Task> taskPage = taskRepository.findByCategoryId(categoryId, pageable);
        List<TaskResponseDTO> content = taskPage.getContent()
                                                .stream()
                                                .map(taskMapper::toResponseDTO)
                                                .toList();
        return new PagedResponseDTO<>(content, taskPage.getNumber(), taskPage.getSize(),
                taskPage.getTotalElements(), taskPage.getTotalPages(), taskPage.isLast());
    }

    // Reescribe findByTagId para que obtenga el Page<Task> con taskRepository.findByTagId, convierta su contenido en TaskResponseDTO con stream y taskMapper::toResponseDTO, y devuelva un PagedResponseDTO con content, number, size, totalElements, totalPages y isLast del Page, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public PagedResponseDTO<TaskResponseDTO> findByTagId(Long tagId, Pageable pageable) {
        Page<Task> taskPage = taskRepository.findByTagId(tagId, pageable);
        List<TaskResponseDTO> content = taskPage.getContent()
                                                .stream()
                                                .map(taskMapper::toResponseDTO)
                                                .toList();
        return new PagedResponseDTO<>(content, taskPage.getNumber(), taskPage.getSize(),
                taskPage.getTotalElements(), taskPage.getTotalPages(), taskPage.isLast());
    }

    // Reescribe findByStatusAndPriority para que obtenga el Page<Task> con taskRepository.findByStatusAndPriority, convierta su contenido en TaskResponseDTO con stream y taskMapper::toResponseDTO, y devuelva un PagedResponseDTO con content, number, size, totalElements, totalPages y isLast del Page, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public PagedResponseDTO<TaskResponseDTO> findByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable) {
        Page<Task> taskPage = taskRepository.findByStatusAndPriority(status, priority, pageable);
        List<TaskResponseDTO> content = taskPage.getContent()
                                                .stream()
                                                .map(taskMapper::toResponseDTO)
                                                .toList();
        return new PagedResponseDTO<>(content, taskPage.getNumber(), taskPage.getSize(),
                taskPage.getTotalElements(), taskPage.getTotalPages(), taskPage.isLast());
    }

    // Reescribe findByDueDateBefore para que obtenga la lista con taskRepository.findByDueDateBefore y convierta cada Task en TaskResponseDTO usando stream, map con taskMapper::toResponseDTO y toList, con Javadoc {@inheritDoc} y @Override. — generado por IA
    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskResponseDTO> findByDueDateBefore(LocalDate date) {
        return taskRepository.findByDueDateBefore(date)
                            .stream()
                            .map(taskMapper::toResponseDTO)
                            .toList();
    }

    // Implementa getCountByStatus(TaskStatus status) devolviendo el resultado de taskRepository.countByStatus(status). Añade Javadoc en español.
    // -generado por IA.
    /**
     * {@inheritDoc}
     */
    @Override
    public long getCountByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }
}
