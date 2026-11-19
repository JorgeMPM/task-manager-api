package com.accenture.taskmanager.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;

/**
 * Repositorio de tareas que proporciona métodos para realizar operaciones CRUD 
 * y consultas personalizadas en la entidad Task.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Encuentra todas las tareas con un estado específico.
     *
     * @param status el estado de la tarea a buscar
     * @return una lista de tareas con el estado especificado
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Encuentra todas las tareas con una prioridad específica.
     *
     * @param priority la prioridad de la tarea a buscar
     * @return una lista de tareas con la prioridad especificada
     */
    List<Task> findByPriority(TaskPriority priority);

    /**
     * Encuentra todas las tareas con un estado y una prioridad específicos con paginación.
     *
     * @param status   el estado de la tarea a buscar
     * @param priority la prioridad de la tarea a buscar
     * @param pageable el objeto Pageable para la paginación
     * @return una página de tareas con el estado y la prioridad especificados
     */
    Page<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);

    /**
     * Encuentra todas las tareas que pertenecen a una categoría específica con paginación.
     * @param categoryId el ID de la categoría a buscar
     * @param pageable el objeto Pageable para la paginación
     * @return una página de tareas que pertenecen a la categoría especificada
     */
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    Page<Task> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Encuentra todas las tareas asociadas a una etiqueta específica con paginación.
     * @param tagId el ID de la etiqueta a buscar
     * @param pageable el objeto Pageable para la paginación
     * @return una página de tareas asociadas a la etiqueta especificada
     */
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.id = :tagId")
    Page<Task> findByTagId(@Param("tagId") Long tagId, Pageable pageable);

    /**
     * Encuentra todas las tareas cuya fecha límite sea anterior a una fecha específica.
     * @param dueDate la fecha límite a comparar
     * @return una lista de tareas cuya fecha límite es anterior a la fecha especificada
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate < :dueDate")
    List<Task> findByDueDateBefore(@Param("dueDate") LocalDate dueDate);

    /**
     * Cuenta cuántas tareas tienen un estado específico.
     * @param status el estado de la tarea a contar
     * @return el número de tareas con el estado especificado
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

    /**
     * Verifica si existe al menos una tarea asociada a una categoría específica por su ID.
     * @param categoryId el ID de la categoría a verificar
     * @return true si existe al menos una tarea asociada a la categoría, false en caso contrario
     */
    boolean existsByCategoryId(Long categoryId);
    
    
}
