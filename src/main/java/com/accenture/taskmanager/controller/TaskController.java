package com.accenture.taskmanager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.ErrorResponse;
import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;
import com.accenture.taskmanager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;


/**
 * Controlador REST que gestiona las peticiones relacionadas con las tareas.
 */
@Tag(name = "Tareas", description = "Operaciones relacionadas con la gestión de tareas")
@RestController
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructor de TaskController que inyecta el servicio TaskService.
     * 
     * @param taskService el servicio de tareas a inyectar.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Método que devuelve una página de tareas según la paginación y orden
     * especificados, envuelta en un PagedResponseDTO.
     * 
     * @param page número de página a devolver.
     * @param size número de tareas por página.
     * @param sort criterio de ordenación.
     * @return respuesta con la tarea correspondiente envuelta en un TaskResponseDTO
     *         y código HTTP 200 OK.
     */
    @Operation(summary = "Obtiene una página de tareas", description = "Devuelve una página de tareas según la paginación y orden especificados. El parámetro 'sort' tiene formato 'campo,dirección' y 'size' está limitado a 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de tareas obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación u ordenación no válidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100),
                Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    /**
     * Método que devuelve una tarea específica por su id.
     * 
     * @param id identificador de la tarea.
     * @return respuesta con la tarea correspondiente envuelta en un TaskResponseDTO
     *         y código HTTP 200 OK, o código HTTP 404 NOT FOUND si no existe.
     */
    @Operation(summary = "Obtiene una tarea por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.findById(id);

        return ResponseEntity.ok(taskResponseDTO);
    }

    /**
     * Método que crea una nueva tarea a partir de un TaskRequestDTO.
     * 
     * @param taskRequestDTO DTO con los datos de la tarea a crear.
     * @return respuesta con la tarea creada envuelta en un TaskResponseDTO y código
     *         HTTP 201 CREATED.
     */
    @Operation(summary = "Crea una nueva tarea", description = "Crea una nueva tarea. El campo 'dueDate' es obligatorio cuando la prioridad es HIGH.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarea creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la tarea inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/v1/tasks")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO createdTask = taskService.create(taskRequestDTO);
        return ResponseEntity.status(201).body(createdTask);
    }

    /**
     * Método que actualiza una tarea existente por su id a partir de un
     * TaskRequestDTO.
     * 
     * @param id             identificador de la tarea a actualizar.
     * @param taskRequestDTO DTO con los datos actualizados de la tarea.
     * @return respuesta con la tarea actualizada envuelta en un TaskResponseDTO y
     *         código HTTP 200 OK.
     */
    @Operation(summary = "Actualiza una tarea existente", description = "Actualiza una tarea existente por su id. El campo 'dueDate' es obligatorio cuando la prioridad es HIGH.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la tarea inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/api/v1/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO updatedTask = taskService.update(id, taskRequestDTO);

        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Método que elimina una tarea existente por su id.
     * 
     * @param id identificador de la tarea a eliminar.
     * @return respuesta vacía con código HTTP 204 NO CONTENT.
     */
    @Operation(summary = "Elimina una tarea existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarea eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/v1/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Método que devuelve una página de tareas de una categoría específica según la
     * paginación y orden especificados, envuelta en un PagedResponseDTO.
     * 
     * @param categoryId identificador de la categoría.
     * @param page       número de página a devolver.
     * @param size       número de tareas por página.
     * @param sort       criterio de ordenación.
     * @return respuesta con la página de tareas envuelta en un PagedResponseDTO y
     *         código HTTP 200 OK.
     */
    @Operation(summary = "Obtiene una página de tareas por categoría", description = "Devuelve una página de tareas de una categoría específica según la paginación y orden especificados. El parámetro 'sort' tiene formato 'campo,dirección', 'size' está limitado a 100 y si la categoría no existe devuelve una página vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks/by-category/{categoryId}")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> getTasksByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100),
                Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        PagedResponseDTO<TaskResponseDTO> tasksPage = taskService.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(tasksPage);
    }

    /**
     * Método que devuelve una página de tareas de una etiqueta específica según la
     * paginación y orden especificados, envuelta en un PagedResponseDTO.
     * 
     * @param tagId identificador de la etiqueta.
     * @param page  número de página a devolver.
     * @param size  número de tareas por página.
     * @param sort  criterio de ordenación.
     * @return respuesta con la página de tareas envuelta en un PagedResponseDTO y
     *         código HTTP 200 OK.
     */
    @Operation(summary = "Obtiene una página de tareas por etiqueta", description = "Devuelve una página de tareas de una etiqueta específica según la paginación y orden especificados. El parámetro 'sort' tiene formato 'campo,dirección', 'size' está limitado a 100 y si la etiqueta no existe devuelve una página vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks/by-tag/{tagId}")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> getTasksByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100),
                Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        PagedResponseDTO<TaskResponseDTO> tasksPage = taskService.findByTagId(tagId, pageable);
        return ResponseEntity.ok(tasksPage);
    }

    /**
     * Método que devuelve una página de tareas de un estado y prioridad específicos
     * según la paginación y orden especificados, envuelta en un PagedResponseDTO.
     * 
     * @param status   el estado de las tareas a buscar.
     * @param priority la prioridad de las tareas a buscar.
     * @param page     número de página.
     * @param size     número de tareas por página.
     * @param sort     criterio de ordenación.
     * @return respuesta con la página de tareas envuelta en un PagedResponseDTO y
     *         código HTTP 200 OK.
     */
    @Operation(summary = "Obtiene una página de tareas por estado y prioridad", description = "Devuelve una página de tareas de un estado y prioridad específicos según la paginación y orden especificados. Los parámetros 'status' y 'priority' son obligatorios, 'sort' tiene formato 'campo,dirección' y 'size' está limitado a 100.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks/by-status-priority")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> getTasksByStatusAndPriority(
            @RequestParam TaskStatus status,
            @RequestParam TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100),
                Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        PagedResponseDTO<TaskResponseDTO> tasksPage = taskService.findByStatusAndPriority(status, priority, pageable);
        return ResponseEntity.ok(tasksPage);
    }

    /**
     * Método que devuelve las tareas cuya fecha límite sea anterior a una fecha
     * específica.
     * 
     * @param date la fecha límite a comparar.
     * @return respuesta con la lista de tareas envuelta en un TaskResponseDTO y
     *         código HTTP 200 OK.
     */
    @Operation(summary = "Obtiene tareas antes de una fecha específica", description = "Devuelve las tareas cuya fecha límite sea anterior a una fecha específica. El parámetro 'date' es obligatorio y tiene formato 'yyyy-MM-dd'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks/before-date")
    public ResponseEntity<List<TaskResponseDTO>> getTasksBeforeDate(@RequestParam LocalDate date) {
        List<TaskResponseDTO> tasks = taskService.findByDueDateBefore(date);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Método que devuelve el número de tareas que tienen un estado específico.
     * 
     * @param status el estado de las tareas a contar.
     * @return respuesta con el número de tareas y código HTTP 200 OK.
     */
    @Operation(summary = "Obtiene el número de tareas por estado", description = "Devuelve el número de tareas que tienen un estado específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Número de tareas obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/v1/tasks/count-by-status")
    public ResponseEntity<Long> getCountByStatus(@RequestParam TaskStatus status) {
        long count = taskService.getCountByStatus(status);
        return ResponseEntity.ok(count);
    }
}
