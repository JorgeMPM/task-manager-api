package com.accenture.taskmanager.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/* Crea la clase record ErrorResponse con los campos: timestamp (LocalDateTime), status (int),
   error (String), message (String), path (String) y errors (List<String>).
   Anotado con @JsonInclude(JsonInclude.Include.NON_NULL) para que errors
   no aparezca en el JSON cuando sea null.
   Añade javadoc a nivel de record en español con un @param por campo. — generado por IA 
*/
/**
 * Representa la respuesta de error que se devuelve al cliente.
 *
 * @param timestamp La fecha y hora en que ocurrió el error.
 * @param status El código de estado HTTP del error.
 * @param error Una breve descripción del error.
 * @param message Un mensaje detallado sobre el error.
 * @param path La ruta de la solicitud que causó el error.
 * @param errors Una lista de errores adicionales, si los hay (puede ser null).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> errors) {
}


