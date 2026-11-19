package com.accenture.taskmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;

/**
 * Clase de test para TaskRepository utilizando @DataJpaTest.
 * Esta clase prueba la capa de persistencia de TaskRepository contra una base de datos H2 embebida en memoria, sin mocks.
 * Los datos de prueba se cargan antes de cada test mediante la anotación @Sql desde el archivo test-data.sql.
 * Cada test se ejecuta de forma aislada y hace rollback al finalizar, asegurando que la base de datos vuelva a su estado inicial.
 */
@DataJpaTest
@ActiveProfiles("test")
@Sql("/test-data.sql")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findAll_shouldReturnAllTasks() {
        // Given
        // Los datos están cargados por test-data.sql

        // When
        List<Task> tasks = taskRepository.findAll();

        // Then
        assertEquals(3, tasks.size());
    }

    @Test
    void findById_shouldReturnTask_whenTaskExists() {
        // Given
        Long taskId = 1L;

        // When
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        // Then
        assertTrue(taskOptional.isPresent());
        assertEquals("Tarea PENDING alta", taskOptional.get().getTitle());
    }

    @Test
    void findById_shouldReturnEmpty_whenTaskDoesNotExist() {
        // Given
        Long taskId = 999L;

        // When
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        // Then
        assertTrue(taskOptional.isEmpty());
    }

    @Test
    void findByStatus_shouldReturnOnlyPendingTasks() {
        // Given
        TaskStatus status = TaskStatus.PENDING;

        // When
        List<Task> tasks = taskRepository.findByStatus(status);

        // Then
        assertEquals(1, tasks.size());
        assertEquals(1L, tasks.get(0).getId());
    }

    @Test
    void findByCategoryId_shouldReturnOnlyTasksOfCategory() {
        // Given
        Long categoryId = 1L;

        // When
        Page<Task> page = taskRepository.findByCategoryId(categoryId, PageRequest.of(0, 10));
        

        // Then
        Set<Long> taskIds = page.getContent().stream().map(Task::getId).collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L), taskIds);
    }

    @Test
    void findByTagId_shouldReturnOnlyTasksWithTag() {
        // Given
        Long tagId = 1L;

        // When
        Page<Task> page = taskRepository.findByTagId(tagId, PageRequest.of(0, 10));

        // Then
        Set<Long> taskIds = page.getContent().stream().map(Task::getId).collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L), taskIds);
    }

    @Test
    void findByDueDateBefore_shouldReturnTasksBeforeDate() {
        // Given
        LocalDate dueDate = LocalDate.of(2026, 6, 10);

        // When
        List<Task> tasks = taskRepository.findByDueDateBefore(dueDate);

        // Then
        Set<Long> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toSet());
        assertEquals(Set.of(1L, 3L), taskIds);
    }

    @Test
    void save_shouldPersistNewTask() {
        // Given
        Task task = new Task();
        task.setTitle("Nueva tarea");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.LOW);

        // When
        Task savedTask = taskRepository.save(task);

        // Then
        assertTrue(savedTask.getId() >= 100L);
        assertEquals(4, taskRepository.findAll().size());
    }

    @Test
    void deleteById_shouldRemoveTask() {
        // Given
        Long taskIdToDelete = 3L;

        // When
        taskRepository.deleteById(taskIdToDelete);

        // Then
        Set<Long> taskIds = taskRepository.findAll().stream().map(Task::getId).collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L), taskIds);
    }
    





}