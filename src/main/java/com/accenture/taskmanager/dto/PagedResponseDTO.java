package com.accenture.taskmanager.dto;

import java.util.List;

/*
Crea un record genérico PagedResponseDTO<T> para las respuestas paginadas de la API con: content (List<T>), pageNumber (int), pageSize (int), totalElements (long), totalPages (int) y last (boolean)
Añade javadoc en español a nivel de clase con un @param por cada componente y por el parámetro de tipo T — generado por IA.
*/
/**
 * DTO de respuesta paginada para la API.
 *
 * @param <T> El tipo de los elementos en el contenido.
 * @param content     La lista de elementos paginados.
 * @param pageNumber  El número de la página actual.
 * @param pageSize    El tamaño de la página.
 * @param totalElements El número total de elementos.
 * @param totalPages  El número total de páginas.
 * @param last        Indica si es la última página.        
 */
public record PagedResponseDTO<T>(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean last) {
}
