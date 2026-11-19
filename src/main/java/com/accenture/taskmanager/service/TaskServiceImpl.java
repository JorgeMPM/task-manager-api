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

    private final CategoryRepository categoryRepository;

    // Repositorio de Spring Data JPA para acceder a las tareas en la base de datos.
    private final TaskRepository taskRepository;

    // Mapper para convertir entre la entidad Task y sus DTOs.
    private final TaskMapper taskMapper;

    // Repositorio para validar y recuperar las etiquetas de una tarea.
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResponseDTO findById(Long id) {
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponseDTO(task);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TaskResponseDTO create(TaskRequestDTO taskRequestDTO) {
        Task task = taskMapper.toEntity(taskRequestDTO);
        if (taskRequestDTO.categoryId() != null) {
        
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
                
                throw new IllegalArgumentException("Tag con id " + missingTagIds + " no encontrado");
            }
            task.setTags(new HashSet<>(tags));
        }
        return taskMapper.toResponseDTO(taskRepository.save(task));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        
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
                
                throw new IllegalArgumentException("Tag con id " + missingTagIds + " no encontrado");
            }
            existingTask.getTags().clear();
            existingTask.getTags().addAll(tags);
        }

        return taskMapper.toResponseDTO(taskRepository.save(existingTask));
    }

    /**
    * {@inheritDoc}
    */
    @Transactional
    @Override
    public void delete(Long id) {   
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCountByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }
}
