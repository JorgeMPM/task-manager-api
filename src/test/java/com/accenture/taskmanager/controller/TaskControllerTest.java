package com.accenture.taskmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.exception.TaskNotFoundException;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;
import com.accenture.taskmanager.security.JwtService;
import com.accenture.taskmanager.service.TaskService;

/*
Crea la clase de test TaskControllerTest con @WebMvcTest(TaskController.class),
@MockitoBean de TaskService taskService e inyecta MockMvc mockMvc con @Autowired.
No uses @SpringBootTest ni accedas a base de datos.
Añade Javadoc en español a nivel de clase a TaskControllerTest, explicando que son tests de la capa web de TaskController con @WebMvcTest,
que las peticiones HTTP se simulan con MockMvc sin arrancar un servidor real,
que TaskService es un mock registrado en el contexto con @MockitoBean
y que se comprueban los códigos HTTP, el JSON de respuesta, la validación
y los errores gestionados por GlobalExceptionHandler.
— generado por IA
*/
/**
 * Clase de test para la capa web de TaskController.
 * Utiliza @WebMvcTest para probar los endpoints del controlador sin arrancar un servidor real.
 * Las peticiones HTTP se simulan con MockMvc, permitiendo verificar los códigos de estado HTTP, el contenido JSON de las respuestas, la validación de datos y la gestión de errores.
 * TaskService se registra como un mock en el contexto de Spring con @MockitoBean, lo que permite controlar su comportamiento durante las pruebas. 
 * JwtService se registra como un mock en el contexto de Spring con @MockitoBean, lo que permite controlar su comportamiento durante las pruebas.
 * Con @AutoConfigureMockMvc(addFilters = false), MockMvc no ejecuta los filtros, de modo que
 * los tests prueban solo el controlador, sin depender del filtro de seguridad.
 */
@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    /*
    Crea un test @Test getTasks_shouldReturnPagedTasks con Given/When/Then:
    crea un TaskResponseDTO con
    new TaskResponseDTO(1L, "Tarea de prueba", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null, List.of())
    y un PagedResponseDTO con new PagedResponseDTO<>(List.of(dto), 0, 10, 1, 1, true),
    taskService.findAll(any(Pageable.class)) devuelve ese PagedResponseDTO,
    ejecuta mockMvc.perform(get("/api/v1/tasks"))
    y comprueba con andExpect status().isOk(),
    jsonPath("$.content[0].title") igual a "Tarea de prueba"
    y jsonPath("$.totalElements") igual a 1.
    — generado por IA
    */
    @Test
    void getTasks_shouldReturnPagedTasks() throws Exception {
        // Given
        TaskResponseDTO dto = new TaskResponseDTO(1L, "Tarea de prueba", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null, List.of());
        PagedResponseDTO<TaskResponseDTO> pagedResponse = new PagedResponseDTO<>(List.of(dto), 0, 10, 1, 1, true);
        when(taskService.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        // When
        ResultActions result = mockMvc.perform(get("/api/v1/tasks"));

        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.content[0].title").value("Tarea de prueba"))
              .andExpect(jsonPath("$.totalElements").value(1));
    }

    /*
    Crea un test @Test getTaskById_shouldReturnTask_whenTaskExists con Given/When/Then.
    En el bloque Given crea un TaskResponseDTO con
    new TaskResponseDTO(1L, "Tarea de prueba", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null, List.of())
    y configura taskService.findById(1L) para que devuelva ese DTO.
    En el bloque When guarda en ResultActions result el resultado de mockMvc.perform(get("/api/v1/tasks/1")).
    En el bloque Then comprueba sobre result con andExpect status().isOk(),
    jsonPath("$.id") igual a 1 y jsonPath("$.title") igual a "Tarea de prueba".
    — generado por IA
    */
    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() throws Exception {
        // Given
        TaskResponseDTO dto = new TaskResponseDTO(1L, "Tarea de prueba", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null, List.of());
        when(taskService.findById(1L)).thenReturn(dto);

        // When
        ResultActions result = mockMvc.perform(get("/api/v1/tasks/1"));

        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(1))
              .andExpect(jsonPath("$.title").value("Tarea de prueba"));
    }

    /*
    Crea un test @Test getTaskById_shouldReturn404_whenTaskNotFound con Given/When/Then.
    En el bloque Given configura taskService.findById(99L) para que lance
    new TaskNotFoundException(99L) con thenThrow.
    En el bloque When guarda en ResultActions result el resultado de mockMvc.perform(get("/api/v1/tasks/99")).
    En el bloque Then comprueba sobre result con andExpect status().isNotFound()
    y jsonPath("$.error") igual a "Not Found".
    — generado por IA
    */
    @Test
    void getTaskById_shouldReturn404_whenTaskNotFound() throws Exception {
        // Given
        when(taskService.findById(99L)).thenThrow(new TaskNotFoundException(99L));

        // When
        ResultActions result = mockMvc.perform(get("/api/v1/tasks/99"));

        // Then
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.error").value("Not Found"));
    }

    /*
    Crea un test @Test createTask_shouldReturn201_whenBodyIsValid con Given/When/Then.
    En el bloque Given crea un String json con un text block de Java con el contenido
    {"title": "Tarea de prueba", "status": "PENDING", "priority": "LOW"},
    crea un TaskResponseDTO con
    new TaskResponseDTO(1L, "Tarea de prueba", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null, List.of())
    y configura taskService.create(any(TaskRequestDTO.class)) para que devuelva ese DTO.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(json)).
    En el bloque Then comprueba sobre result con andExpect status().isCreated()
    y jsonPath("$.id") igual a 1.
    — generado por IA
    */
    @Test
    void createTask_shouldReturn201_whenBodyIsValid() throws Exception {
        // Given
        String json = """
            {"title": "Tarea de prueba", "status": "PENDING", "priority": "LOW"}
            """;
        TaskResponseDTO dto = new TaskResponseDTO(1L, "Tarea de prueba", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null, List.of());
        when(taskService.create(any(TaskRequestDTO.class))).thenReturn(dto);

        // When
        ResultActions result = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Then
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.id").value(1));
    }

    /*
    Crea un test @Test createTask_shouldReturn400_whenTitleIsBlank con Given/When/Then.
    En el bloque Given crea un String json con un text block de Java con el contenido
    {"title": "", "status": "PENDING", "priority": "LOW"}.
    No configures ningún comportamiento de taskService.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(json)).
    En el bloque Then comprueba sobre result con andExpect status().isBadRequest(),
    jsonPath("$.message") igual a "Error de validación"
    y jsonPath("$.errors[0]") igual a "title: El título es obligatorio",
    y con verify y never() que nunca se llamó a taskService.create con any(TaskRequestDTO.class).
    — generado por IA
    */
    @Test
    void createTask_shouldReturn400_whenTitleIsBlank() throws Exception {
        // Given
        String json = """
            {"title": "", "status": "PENDING", "priority": "LOW"}
            """;

        // When
        ResultActions result = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Then
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.message").value("Error de validación"))
              .andExpect(jsonPath("$.errors[0]").value("title: El título es obligatorio"));

        verify(taskService, never()).create(any(TaskRequestDTO.class));
    }

    /*
    Crea un test @Test createTask_shouldReturn400_whenStatusIsInvalid con Given/When/Then.
    En el bloque Given crea un String json con un text block de Java con el contenido
    {"title": "Tarea de prueba", "status": "XXX", "priority": "LOW"}.
    No configures ningún comportamiento de taskService.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON).content(json)).
    En el bloque Then comprueba sobre result con andExpect status().isBadRequest()
    y jsonPath("$.message") igual a "El valor 'XXX' no es válido para el campo 'status'",
    y con verify y never() que nunca se llamó a taskService.create con any(TaskRequestDTO.class).
    — generado por IA
    */
    @Test
    void createTask_shouldReturn400_whenStatusIsInvalid() throws Exception {
        // Given
        String json = """
            {"title": "Tarea de prueba", "status": "XXX", "priority": "LOW"}
            """;

        // When
        ResultActions result = mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Then
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.message").value("El valor 'XXX' no es válido para el campo 'status'"));

        verify(taskService, never()).create(any(TaskRequestDTO.class));
    }

    /*
    Crea un test @Test updateTask_shouldReturn200_whenTaskExists con Given/When/Then.
    En el bloque Given crea un String json con un text block de Java con el contenido
    {"title": "Tarea actualizada", "status": "IN_PROGRESS", "priority": "LOW"},
    crea un TaskResponseDTO con
    new TaskResponseDTO(1L, "Tarea actualizada", null, TaskStatus.IN_PROGRESS, TaskPriority.LOW, null, null, null, List.of())
    y configura taskService.update(eq(1L), any(TaskRequestDTO.class)) para que devuelva ese DTO.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(put("/api/v1/tasks/1").contentType(MediaType.APPLICATION_JSON).content(json)).
    En el bloque Then comprueba sobre result con andExpect status().isOk(),
    jsonPath("$.title") igual a "Tarea actualizada" y jsonPath("$.status") igual a "IN_PROGRESS".
    — generado por IA
    */
    @Test
    void updateTask_shouldReturn200_whenTaskExists() throws Exception {
        // Given
        String json = """
            {"title": "Tarea actualizada", "status": "IN_PROGRESS", "priority": "LOW"}
            """;

        TaskResponseDTO expectedDTO = new TaskResponseDTO(1L, "Tarea actualizada", null, TaskStatus.IN_PROGRESS, TaskPriority.LOW, null, null, null, List.of());

        when(taskService.update(eq(1L), any(TaskRequestDTO.class))).thenReturn(expectedDTO);

        // When
        ResultActions result = mockMvc.perform(put("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.title").value("Tarea actualizada"))
              .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

    }

    /*
    Crea un test @Test updateTask_shouldReturn404_whenTaskNotFound con Given/When/Then.
    En el bloque Given crea un String json con un text block de Java con el contenido
    {"title": "Tarea actualizada", "status": "IN_PROGRESS", "priority": "LOW"
    y configura taskService.update(eq(99L), any(TaskRequestDTO.class)) para que lance
    new TaskNotFoundException(99L) con thenThrow.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(put("/api/v1/tasks/99").contentType(MediaType.APPLICATION_JSON).content(json)).
    En el bloque Then comprueba sobre result con andExpect status().isNotFound()
    y jsonPath("$.error") igual a "Not Found".
    — generado por IA
    */
    @Test
    void updateTask_shouldReturn404_whenTaskNotFound() throws Exception {
        // Given
        String json = """
            {"title": "Tarea actualizada", "status": "IN_PROGRESS", "priority": "LOW"}
            """;

        when(taskService.update(eq(99L), any(TaskRequestDTO.class))).thenThrow(new TaskNotFoundException(99L));

        // When
        ResultActions result = mockMvc.perform(put("/api/v1/tasks/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // Then
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.error").value("Not Found"));
    }

    /*
    Crea un test @Test deleteTask_shouldReturn204_whenTaskExists con Given/When/Then.
    En el bloque Given no configures nada: taskService.delete es void y el mock no hace nada por defecto.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(delete("/api/v1/tasks/1")).
    En el bloque Then comprueba sobre result con andExpect status().isNoContent()
    y con verify que se llamó a taskService.delete(1L).
    — generado por IA
    */
    @Test
    void deleteTask_shouldReturn204_whenTaskExists() throws Exception {
        // Given
        // No se necesita configurar nada, el mock de delete es void y no hace nada por defecto.

        // When
        ResultActions result = mockMvc.perform(delete("/api/v1/tasks/1"));

        // Then
        result.andExpect(status().isNoContent());
        verify(taskService).delete(1L);
    }

    /*
    Crea un test @Test deleteTask_shouldReturn404_whenTaskNotFound con Given/When/Then.
    En el bloque Given configura con doThrow(new TaskNotFoundException(99L)).when(taskService).delete(99L)
    que el método void delete lance la excepción.
    En el bloque When guarda en ResultActions result el resultado de
    mockMvc.perform(delete("/api/v1/tasks/99")).
    En el bloque Then comprueba sobre result con andExpect status().isNotFound()
    y jsonPath("$.error") igual a "Not Found".
    — generado por IA
    */
    @Test
    void deleteTask_shouldReturn404_whenTaskNotFound() throws Exception {
        // Given
        doThrow(new TaskNotFoundException(99L)).when(taskService).delete(99L);

        // When
        ResultActions result = mockMvc.perform(delete("/api/v1/tasks/99"));

        // Then
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.error").value("Not Found"));
    }
    



}