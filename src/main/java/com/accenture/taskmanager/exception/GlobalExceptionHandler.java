package com.accenture.taskmanager.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.accenture.taskmanager.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;


/**
 * Maneja las excepciones de manera global en la API y devuelve respuestas de error consistentes.
 * Esta clase está anotada con @RestControllerAdvice para interceptar excepciones lanzadas por cualquier Controller.
 * Utiliza un Logger de SLF4J para registrar información sobre los errores.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Construye una respuesta de error consistente para el cliente.
     *
     * @param status El código de estado HTTP del error.
     * @param message Un mensaje detallado sobre el error.
     * @param request La solicitud que causó el error.
     * @param errors Una lista de errores adicionales, si los hay (puede ser null).
     * @return Una respuesta de error con la información proporcionada.
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request, List<String> errors) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Maneja la excepción TaskNotFoundException y devuelve una respuesta de error con estado 404.
     * @param ex La excepción lanzada cuando no se encuentra la tarea.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 404 y detalles del error.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request) {
        
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /**
     * Maneja la excepción NoSuchElementException y devuelve una respuesta de error con estado 404.
     * @param ex La excepción lanzada cuando no se encuentra un elemento.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 404 y detalles del error.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /**
     * Maneja la excepción MethodArgumentNotValidException y devuelve una respuesta de error con estado 400.
     * @param ex La excepción lanzada cuando la validación de argumentos falla.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 400 y detalles de los errores de validación.
     */ 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> error instanceof FieldError fieldError
                    ? fieldError.getField() + ": " + fieldError.getDefaultMessage()
                    : error.getDefaultMessage())
            .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación", request, errors);
    }

    /**
     * Maneja la excepción HttpMessageNotReadableException y devuelve una respuesta de error con estado 400.
     * @param ex La excepción lanzada cuando el cuerpo de la solicitud no se puede leer o parsear.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 400 y detalles del error de lectura del mensaje.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "El cuerpo de la petición está mal formado";
        if (ex.getCause() instanceof InvalidFormatException ife && !ife.getPath().isEmpty()) {
            String field = ife.getPath().get(ife.getPath().size() - 1).getPropertyName();
            message = "El valor '" + ife.getValue() + "' no es válido para el campo '" + field + "'";
        }
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    /**
     * Maneja la excepción IllegalArgumentException y devuelve una respuesta de error con estado 400.
     * @param ex La excepción lanzada cuando se pasa un argumento ilegal o inapropiado.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 400 y detalles del error de argumento ilegal
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    /**
     * Maneja la excepción MethodArgumentTypeMismatchException y devuelve una respuesta de error con estado 400.
     * @param ex La excepción lanzada cuando hay un desajuste de tipo en los argumentos del método.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 400 y detalles del error de desajuste de tipo.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "El valor '" + ex.getValue() + "' no es válido para el parámetro '" + ex.getName() + "'";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    /**
     * Maneja la excepción MissingServletRequestParameterException y devuelve una respuesta de error con estado 400.
     * @param ex La excepción lanzada cuando falta un parámetro obligatorio en la solicitud.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 400 y detalles del parámetro faltante.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = "Falta el parámetro obligatorio '" + ex.getParameterName() + "'";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    /**
     * Maneja la excepción PropertyReferenceException y devuelve una respuesta de error con estado 400.
     * @param ex La excepción lanzada cuando se hace referencia a un campo de ordenación no válido.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 400 y detalles del campo de ordenación no válido.
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReference(PropertyReferenceException ex, HttpServletRequest request) {
        String message = "El campo de ordenación '" + ex.getPropertyName() + "' no es válido";
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    /**
     * Maneja la excepción NoResourceFoundException y devuelve una respuesta de error con estado 404.
     * @param ex La excepción lanzada cuando no se encuentra el recurso solicitado.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 404 y un mensaje indicando que la ruta solicitada no existe.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request, null);
    }

    /**
     * Maneja la excepción HttpRequestMethodNotSupportedException y devuelve una respuesta de error con estado 405.
     * @param ex La excepción lanzada cuando se utiliza un método HTTP no permitido en la ruta solicitada.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 405 y un mensaje indicando que el método no está permitido en la ruta solicitada.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = "El método '" + ex.getMethod() + "' no está permitido en esta ruta";
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message, request, null);
    }

    /**
     * Maneja la excepción ResponseStatusException y devuelve una respuesta de error con el estado correspondiente.
     * @param ex La excepción lanzada con un estado HTTP específico.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con el estado HTTP especificado en la excepción y un mensaje detallado.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildResponse(status, ex.getReason(), request, null);
    }

    /**
     * Maneja cualquier excepción no controlada y devuelve una respuesta de error con estado 500.
     * Registra el error en el log para su análisis posterior. 
     * @param ex La excepción no controlada que se ha lanzado.
     * @param request La solicitud que causó la excepción.
     * @return Una respuesta de error con estado 500 y un mensaje indicando un error interno del servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request, null);
    }
}