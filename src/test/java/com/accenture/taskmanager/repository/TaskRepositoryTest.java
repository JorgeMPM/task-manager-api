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

/*
Crea la clase de test TaskRepositoryTest con @DataJpaTest, @ActiveProfiles("test")
y @Sql("/test-data.sql"), e inyecta TaskRepository con @Autowired.
No uses @SpringBootTest ni mocks.
Añade Javadoc en español a nivel de clase a TaskRepositoryTest, explicando que son tests de la capa de persistencia de TaskRepository con @DataJpaTest,
que se ejecutan contra una base de datos H2 embebida en memoria, sin mocks,
que los datos se cargan antes de cada test con @Sql desde test-data.sql y que cada test hace rollback al terminar.
— generado por IA
*/
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

    /*
    Crea un test @Test findAll_shouldReturnAllTasks con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta taskRepository.findAll()
    y comprueba con assertEquals que devuelve 3 tareas.
    — generado por IA
    */
    @Test
    void findAll_shouldReturnAllTasks() {
        // Given
        // Los datos están cargados por test-data.sql

        // When
        List<Task> tasks = taskRepository.findAll();

        // Then
        assertEquals(3, tasks.size());
    }

    /*
    Crea un test @Test findById_shouldReturnTask_whenTaskExists con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta taskRepository.findById(1L)
    y comprueba con assertTrue que el Optional está presente
    y con assertEquals que el título es "Tarea PENDING alta".
    — generado por IA
    */
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

    /*
    Crea un test @Test findById_shouldReturnEmpty_whenTaskDoesNotExist con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta taskRepository.findById(999L)
    y comprueba con assertTrue que el Optional está vacío usando isEmpty().
    — generado por IA
    */
    @Test
    void findById_shouldReturnEmpty_whenTaskDoesNotExist() {
        // Given
        Long taskId = 999L;

        // When
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        // Then
        assertTrue(taskOptional.isEmpty());
    }

    /*
    Crea un test @Test findByStatus_shouldReturnOnlyPendingTasks con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta taskRepository.findByStatus(TaskStatus.PENDING),
    comprueba con assertEquals que devuelve 1 tarea
    y con assertEquals que el id de esa tarea es 1L.
    — generado por IA
    */
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

    /*
    Crea un test @Test findByCategoryId_shouldReturnOnlyTasksOfCategory con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta
    taskRepository.findByCategoryId(1L, PageRequest.of(0, 10)),
    obtiene los ids del contenido de la página en un Set<Long> con stream, map(Task::getId)
    y Collectors.toSet(),
    y comprueba con assertEquals que ese Set es igual a Set.of(1L, 2L).
    — generado por IA
    */
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

    /*
    Crea un test @Test findByTagId_shouldReturnOnlyTasksWithTag con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta
    taskRepository.findByTagId(1L, PageRequest.of(0, 10)),
    obtiene en el bloque Then los ids del contenido de la página en un Set<Long> con stream,
    map(Task::getId) y Collectors.toSet(),
    y comprueba con assertEquals que ese Set es igual a Set.of(1L, 2L).
    — generado por IA
    */
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

    /*
    Crea un test @Test findByDueDateBefore_shouldReturnTasksBeforeDate con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta
    taskRepository.findByDueDateBefore(LocalDate.of(2026, 6, 10)),
    obtiene en el bloque Then los ids de las tareas en un Set<Long> con stream,
    map(Task::getId) y Collectors.toSet(),
    y comprueba con assertEquals que ese Set es igual a Set.of(1L, 3L).
    — generado por IA
    */
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

    /*
    Crea un test @Test save_shouldPersistNewTask con Given/When/Then:
    crea una Task con new Task() y le asigna con setters el título "Nueva tarea",
    el status TaskStatus.PENDING y la priority TaskPriority.LOW,
    ejecuta taskRepository.save(task),
    comprueba con assertTrue que el id de la tarea guardada es mayor o igual que 100L
    y con assertEquals que taskRepository.findAll() devuelve ahora 4 tareas.
    — generado por IA
    */
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

    /*
    Crea un test @Test deleteById_shouldRemoveTask con Given/When/Then:
    con los datos cargados por test-data.sql, ejecuta taskRepository.deleteById(3L),
    obtiene en el bloque Then los ids de taskRepository.findAll() en un Set<Long> con stream,
    map(Task::getId) y Collectors.toSet(),
    y comprueba con assertEquals que ese Set es igual a Set.of(1L, 2L).
    — generado por IA
    */
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