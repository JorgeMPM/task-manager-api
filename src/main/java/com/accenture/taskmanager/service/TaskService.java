package com.accenture.taskmanager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.exception.TaskNotFoundException;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;

/*Crea una interfaz TaskService con cinco métodos para gestionar tareas:
- findAll(): devuelve una List<Task> con todas las tareas.
- findById(int id): devuelve la Task correspondiente, o null si no existe.
- create(Task task): añade la tarea y la devuelve.
- update(int id, Task updatedTask): si existe, la sustituye manteniendo el id y la devuelve; si no existe, devuelve null.
- delete(int id): elimina la tarea y devuelve true; si no existe, devuelve false.
Añade Javadoc a la interfaz y a cada método. No uses clases de HTTP ni Optional.
— generado por IA.
*/

/**
 * Interfaz TaskService que define las operaciones de negocio para gestionar tareas.
 * 
 * @author jorge martin perez moreno
 * @version 1.0
 */
public interface TaskService {

    // Crea la firma del método findAll para que reciba un Pageable y devuelva un PagedResponseDTO<TaskResponseDTO>, con Javadoc en español con @param y @return. — generado por IA
    /**
     * Devuelve una página de tareas según la paginación especificada, envuelta en un PagedResponseDTO.
     * @param pageable el objeto Pageable para la paginación.
     * @return PagedResponseDTO<TaskResponseDTO> con las tareas según la paginación.
     */
    PagedResponseDTO<TaskResponseDTO> findAll(Pageable pageable);

    // Crea la firma del método findById para que reciba un Long id y devuelva un TaskResponseDTO, con Javadoc en español con @param, @return y @throws indicando que lanza ResponseStatusException si la tarea no existe. — generado por IA
    /**
     * Devuelve la tarea correspondiente al id proporcionado.
     * @param id el id de la tarea a buscar.
     * @return TaskResponseDTO correspondiente, o lanza ResponseStatusException si no existe.
     * @throws ResponseStatusException si la tarea no existe.
     */
    TaskResponseDTO findById(Long id);
       
    // Crea la firma del método create para que reciba un TaskRequestDTO taskRequestDTO y devuelva un TaskResponseDTO con la tarea creada, con Javadoc en español con @param y @return. — generado por IA
    /**
     * Crea una nueva tarea a partir del TaskRequestDTO proporcionado y devuelve un TaskResponseDTO con la tarea creada.
     * @param taskRequestDTO el DTO con los datos de la tarea a crear.
     * @return TaskResponseDTO con la tarea creada.
     */
    TaskResponseDTO create(TaskRequestDTO taskRequestDTO);

    // Crea la firma del método update para que reciba un Long id y un TaskRequestDTO taskRequestDTO y devuelva un TaskResponseDTO con la tarea actualizada o null si no existe, con Javadoc en español con @param para cada parámetro y @return. — generado por IA
    /**
     * Actualiza la tarea correspondiente al id proporcionado con los datos del TaskRequestDTO.
     * @param id el id de la tarea a actualizar.
     * @param taskRequestDTO el DTO con los datos de la tarea a actualizar.
     * @return TaskResponseDTO con la tarea actualizada, o null si no existe.
     */
    TaskResponseDTO update(Long id, TaskRequestDTO taskRequestDTO);

    /**
     * Elimina la tarea correspondiente al id proporcionado.
     * @param id el id de la tarea a eliminar.
     * @throws TaskNotFoundException si no existe una tarea con ese id
     */
    void delete(Long id);

    // Crea la firma del método findByStatus para que reciba un TaskStatus status y devuelva una List<TaskResponseDTO>, con Javadoc en español con @param y @return. — generado por IA
    /**
     * Devuelve una lista de tareas que tengan el estado indicado.
     * @param status el estado de las tareas a buscar.
     * @return List<TaskResponseDTO> con las tareas que tengan el estado especificado.
     */
    List<TaskResponseDTO> findByStatus(TaskStatus status);

    // Crea la firma del método findByPriority para que reciba un TaskPriority priority y devuelva una List<TaskResponseDTO>, con Javadoc en español con @param y @return. — generado por IA
    /**
     * Devuelve una lista de tareas que tengan la prioridad indicada.
     * @param priority la prioridad de las tareas a buscar.
     * @return List<TaskResponseDTO> con las tareas que tengan la prioridad especificada.
     */
    List<TaskResponseDTO> findByPriority(TaskPriority priority);

    // Crea la firma del método findByCategoryId para que reciba un Long categoryId y un Pageable pageable y devuelva un PagedResponseDTO<TaskResponseDTO>, con Javadoc en español con @param para cada parámetro y @return. — generado por IA
    /**
     * Devuelve una página de tareas asociadas a la categoría indicada, envuelta en un PagedResponseDTO.
     * @param categoryId el ID de la categoría a buscar.
     * @param pageable el objeto Pageable para la paginación.
     * @return PagedResponseDTO<TaskResponseDTO> con las tareas asociadas a la categoría especificada.
     */
    PagedResponseDTO<TaskResponseDTO> findByCategoryId(Long categoryId, Pageable pageable);

    // Crea la firma del método findByTagId para que reciba un Long tagId y un Pageable pageable y devuelva un PagedResponseDTO<TaskResponseDTO>, con Javadoc en español con @param para cada parámetro y @return. — generado por IA
    /**
     * Devuelve una página de tareas asociadas a la etiqueta indicada, envuelta en un PagedResponseDTO.
     * @param tagId el ID de la etiqueta a buscar.
     * @param pageable el objeto Pageable para la paginación.
     * @return PagedResponseDTO<TaskResponseDTO> con las tareas asociadas a la etiqueta especificada.
     */
    PagedResponseDTO<TaskResponseDTO> findByTagId(Long tagId, Pageable pageable);

    // Crea la firma del método findByStatusAndPriority para que reciba un TaskStatus status, un TaskPriority priority y un Pageable pageable y devuelva un PagedResponseDTO<TaskResponseDTO>, con Javadoc en español con @param para cada parámetro y @return. — generado por IA
    /**
     * Devuelve una página de tareas que coincidan con el estado y la prioridad indicados, envuelta en un PagedResponseDTO.
     * @param status   el estado de las tareas a buscar.
     * @param priority la prioridad de las tareas a buscar.
     * @param pageable el objeto Pageable para la paginación.
     * @return PagedResponseDTO<TaskResponseDTO> con las tareas que coincidan con el estado y la prioridad especificados.
     */
    PagedResponseDTO<TaskResponseDTO> findByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);

    // Crea la firma del método findByDueDateBefore para que reciba un LocalDate date y devuelva una List<TaskResponseDTO>, con Javadoc en español con @param y @return. — generado por IA
    /**
     * Devuelve una lista de tareas cuya fecha límite sea anterior a la fecha indicada.
     * @param date la fecha límite a comparar.
     * @return List<TaskResponseDTO> con las tareas cuya fecha límite es anterior a la fecha especificada.
     */
    List<TaskResponseDTO> findByDueDateBefore(LocalDate date);

    // Añade un método getCountByStatus que reciba un TaskStatus status y devuelva un long con el número de tareas que tienen ese estado. Añade Javadoc en español.
    // -generado por IA.
    /**
     * Devuelve el número de tareas que tienen el estado indicado.
     * @param status el estado de las tareas a contar.
     * @return long con el número de tareas que tienen el estado especificado.
     */
    long getCountByStatus(TaskStatus status);

}
