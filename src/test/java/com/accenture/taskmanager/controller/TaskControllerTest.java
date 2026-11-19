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