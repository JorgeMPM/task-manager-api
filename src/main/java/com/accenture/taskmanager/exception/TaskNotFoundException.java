package com.accenture.taskmanager.exception;


/**
 * Excepción lanzada cuando no se encuentra una tarea con un id específico.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructor de la excepción que recibe el id de la tarea no encontrada.
     *
     * @param id El id de la tarea que no se encontró.
     */
    public TaskNotFoundException(Long id) {
        super("Tarea con id " + id + " no encontrada");
    }
}