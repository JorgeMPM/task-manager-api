package com.accenture.taskmanager.exception;

/* Crea la clase TaskNotFoundException que extiende RuntimeException.
   Constructor que recibe el id (Long) y llama a super con el mensaje
   "Tarea con id " + id + " no encontrada".
   Añade javadoc a nivel de clase y a nivel de constructor en español. — generado por IA */

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