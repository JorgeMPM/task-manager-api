package com.accenture.taskmanager.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.exception.TaskNotFoundException;
import com.accenture.taskmanager.mapper.TaskMapper;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.repository.CategoryRepository;
import com.accenture.taskmanager.repository.TagRepository;
import com.accenture.taskmanager.repository.TaskRepository;

/**
 * Clase de test unitario para TaskServiceImpl utilizando JUnit y Mockito.
 * Esta clase prueba la lógica de negocio del servicio de tareas de forma aislada, sin necesidad de un contexto de Spring.
 * Los repositorios (TaskRepository, CategoryRepository, TagRepository) y el TaskMapper son mocks creados con @Mock
 * e inyectados en el Service mediante @InjectMocks.
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks   
    private TaskServiceImpl taskService;

    @Test
    void findAll_shouldReturnPagedResponse_whenTasksExist() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        TaskResponseDTO expectedDTO = new TaskResponseDTO(1L, "Tarea de prueba", null, null, null, null, null, null, List.of());

        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.toResponseDTO(task)).thenReturn(expectedDTO);

        // When
        PagedResponseDTO<TaskResponseDTO> result = taskService.findAll(pageable);

        // Then
        assertEquals(List.of(expectedDTO), result.content());
    }

    @Test
    void findById_shouldReturnTask_whenTaskExists() {
        // Given
        Task task = new Task();
        TaskResponseDTO expectedDTO = new TaskResponseDTO(1L, "Tarea de prueba", null, null, null, null, null, null, List.of());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDTO(task)).thenReturn(expectedDTO);

        // When
        TaskResponseDTO result = taskService.findById(1L);

        // Then
        assertEquals(expectedDTO, result);
    }

    @Test
    void findById_shouldThrowException_whenTaskNotFound() {
        // Given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.findById(99L);
        });
    }

    @Test
    void create_shouldPersistTask_whenInputIsValid() {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("Tarea de prueba", null, null, null, null, null, null);
        Task task = new Task();
        TaskResponseDTO expectedDTO = new TaskResponseDTO(1L, "Tarea de prueba", null, null, null, null, null, null, List.of());

        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDTO(task)).thenReturn(expectedDTO);

        // When
        TaskResponseDTO result = taskService.create(request);

        // Then
        assertEquals(expectedDTO, result);
        verify(taskRepository).save(task);
    }

    @Test
    void update_shouldUpdateTask_whenTaskExists() {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("Título actualizado", null, null, null, null, null, null);
        Task existingTask = new Task();
        TaskResponseDTO expectedDTO = new TaskResponseDTO(1L, "Título actualizado", null, null, null, null, null, null, List.of());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);
        when(taskMapper.toResponseDTO(existingTask)).thenReturn(expectedDTO);

        // When
        TaskResponseDTO result = taskService.update(1L, request);

        // Then
        assertEquals(expectedDTO, result);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void update_shouldThrowException_whenTaskNotFound() {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("Título actualizado", null, null, null, null, null, null);
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.update(99L, request);
        });
    }

    @Test
    void delete_shouldDeleteTask_whenTaskExists() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);

        // When
        taskService.delete(1L);

        // Then
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenTaskNotFound() {
        // Given
        when(taskRepository.existsById(99L)).thenReturn(false);

        // When / Then
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.delete(99L);
        });

        // Verify that deleteById was never called
        verify(taskRepository, never()).deleteById(99L);
    }

    @Test
    void create_shouldThrowException_whenCategoryNotFound() {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("Tarea de prueba", null, null, null, null, 5L, null);
        when(taskMapper.toEntity(request)).thenReturn(new Task());
        when(categoryRepository.findById(5L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.create(request);
        });

        // Verify that save was never called
        verify(taskRepository, never()).save(org.mockito.ArgumentMatchers.any(Task.class));
    }

    @Test
    void create_shouldThrowException_whenTagNotFound() {
        // Given
        Set<Long> tagIds = Set.of(1L, 2L);
        TaskRequestDTO request = new TaskRequestDTO("Tarea de prueba", null, null, null, null, null, tagIds);
        when(taskMapper.toEntity(request)).thenReturn(new Task());
        when(tagRepository.findAllById(tagIds)).thenReturn(List.of());

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.create(request);
        });

        // Verify that save was never called
        verify(taskRepository, never()).save(org.mockito.ArgumentMatchers.any(Task.class));
    }







    
}
