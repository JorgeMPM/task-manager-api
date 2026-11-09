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

/*
Crea la clase de test TaskServiceImplTest con @ExtendWith(MockitoExtension.class),
@Mock de TaskRepository, CategoryRepository, TagRepository y TaskMapper,
e @InjectMocks de TaskServiceImpl. Sin anotaciones de Spring.
Añade javadoc a nivel de clase, en español explicando que son tests unitarios de TaskServiceImpl con JUnit y Mockito, 
sin contexto de Spring, que los repositorios y TaskMapper son mocks inyectados con @InjectMocks
y que se comprueba la lógica de negocio del Service de forma aislada.
— generado por IA
*/
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

    /*
    Crea un test @Test findAll_shouldReturnPagedResponse_whenTasksExist con Given/When/Then:
    taskRepository.findAll(pageable) devuelve un PageImpl con una Task,
    taskMapper.toResponseDTO(task) devuelve un TaskResponseDTO,
    y comprueba que el content del resultado contiene ese DTO.
    — generado por IA
    */
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

    /*
    Crea un test @Test findById_shouldReturnTask_whenTaskExists con Given/When/Then:
    crea una Task con new Task() y un TaskResponseDTO con
    new TaskResponseDTO(1L, "Tarea de prueba", null, null, null, null, null, null, List.of()),
    taskRepository.findById(1L) devuelve Optional.of(task),
    taskMapper.toResponseDTO(task) devuelve ese TaskResponseDTO,
    ejecuta taskService.findById(1L)
    y comprueba con assertEquals que el resultado es ese TaskResponseDTO.
    — generado por IA
    */
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

    /*
    Crea un test @Test findById_shouldThrowException_whenTaskNotFound con Given/When/Then:
    taskRepository.findById(99L) devuelve Optional.empty(),
    y comprueba con assertThrows que taskService.findById(99L)
    lanza TaskNotFoundException.
    — generado por IA
    */
    @Test
    void findById_shouldThrowException_whenTaskNotFound() {
        // Given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.findById(99L);
        });
    }

    /*
    Crea un test @Test create_shouldPersistTask_whenInputIsValid con Given/When/Then:
    crea un TaskRequestDTO con new TaskRequestDTO("Tarea de prueba", null, null, null, null, null, null),
    una Task con new Task() y un TaskResponseDTO con
    new TaskResponseDTO(1L, "Tarea de prueba", null, null, null, null, null, null, List.of()),
    taskMapper.toEntity(request) devuelve la task,
    taskRepository.save(task) devuelve la task,
    taskMapper.toResponseDTO(task) devuelve el TaskResponseDTO,
    ejecuta taskService.create(request),
    comprueba con assertEquals que el resultado es ese TaskResponseDTO
    y con verify que se llamó a taskRepository.save(task).
    — generado por IA
    */
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

     /*
    Crea un test @Test update_shouldUpdateTask_whenTaskExists con Given/When/Then:
    crea un TaskRequestDTO con new TaskRequestDTO("Título actualizado", null, null, null, null, null, null),
    una Task existente con new Task() y un TaskResponseDTO con
    new TaskResponseDTO(1L, "Título actualizado", null, null, null, null, null, null, List.of()),
    taskRepository.findById(1L) devuelve Optional.of(existingTask),
    taskRepository.save(existingTask) devuelve la existingTask,
    taskMapper.toResponseDTO(existingTask) devuelve el TaskResponseDTO,
    ejecuta taskService.update(1L, request),
    comprueba con assertEquals que el resultado es ese TaskResponseDTO
    y con verify que se llamó a taskRepository.save(existingTask).
    — generado por IA
    */
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

    /*
    Crea un test @Test update_shouldThrowException_whenTaskNotFound con Given/When/Then:
    crea un TaskRequestDTO con new TaskRequestDTO("Título actualizado", null, null, null, null, null, null),
    taskRepository.findById(99L) devuelve Optional.empty(),
    y comprueba con assertThrows que taskService.update(99L, request)
    lanza TaskNotFoundException.
    — generado por IA
    */
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

    /*
    Crea un test @Test delete_shouldDeleteTask_whenTaskExists con Given/When/Then:
    taskRepository.existsById(1L) devuelve true,
    ejecuta taskService.delete(1L)
    y comprueba con verify que se llamó a taskRepository.deleteById(1L).
    — generado por IA
    */
    @Test
    void delete_shouldDeleteTask_whenTaskExists() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);

        // When
        taskService.delete(1L);

        // Then
        verify(taskRepository).deleteById(1L);
    }

    /*
    Crea un test @Test delete_shouldThrowException_whenTaskNotFound con Given/When/Then:
    taskRepository.existsById(99L) devuelve false,
    comprueba con assertThrows que taskService.delete(99L) lanza TaskNotFoundException
    y con verify y never() que nunca se llamó a taskRepository.deleteById(99L).
    — generado por IA
    */
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

    /*
    Crea un test @Test create_shouldThrowException_whenCategoryNotFound con Given/When/Then:
    crea un TaskRequestDTO con new TaskRequestDTO("Tarea de prueba", null, null, null, null, 5L, null),
    taskMapper.toEntity(request) devuelve new Task(),
    categoryRepository.findById(5L) devuelve Optional.empty(),
    comprueba con assertThrows que taskService.create(request) lanza IllegalArgumentException
    y con verify y never() que nunca se llamó a taskRepository.save con any(Task.class).
    — generado por IA
    */
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

    /*
    Crea un test @Test create_shouldThrowException_whenTagNotFound con Given/When/Then:
    crea un Set<Long> tagIds con Set.of(1L, 2L)
    y un TaskRequestDTO con new TaskRequestDTO("Tarea de prueba", null, null, null, null, null, tagIds),
    taskMapper.toEntity(request) devuelve new Task(),
    tagRepository.findAllById(tagIds) devuelve List.of(),
    comprueba con assertThrows que taskService.create(request) lanza IllegalArgumentException
    y con verify y never() que nunca se llamó a taskRepository.save con any(Task.class).
    — generado por IA
    */
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
