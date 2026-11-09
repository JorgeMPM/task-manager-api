package com.accenture.taskmanager.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;


/* Crea la interfaz TagService con tres métodos para gestionar etiquetas:
- findAll(): devuelve una List<TagResponseDTO> con todas las etiquetas.
- findById(Long id): devuelve el TagResponseDTO correspondiente; lanza ResponseStatusException si no existe.
- create(TagRequestDTO tagRequestDTO): crea la etiqueta y devuelve el TagResponseDTO creado.
Añade javadoc en español a la interfaz y a cada método con @param, @return y @throws donde corresponda. — generado por IA */
/**
 * Interfaz TagService que define las operaciones de negocio para gestionar etiquetas.
 */
public interface TagService {
    
    /**
     * Devuelve una lista con todas las etiquetas.
     *
     * @return Una lista de {@code TagResponseDTO} con todas las etiquetas.
     */
    List<TagResponseDTO> findAll();

    /**
     * Devuelve la etiqueta correspondiente al ID proporcionado.
     *
     * @param id El ID de la etiqueta a buscar.
     * @return El {@code TagResponseDTO} correspondiente a la etiqueta.
     * @throws NoSuchElementException si no existe una etiqueta con ese id
     */
    TagResponseDTO findById(Long id);

    /**
     * Crea una nueva etiqueta a partir de los datos proporcionados.
     *
     * @param tagRequestDTO Los datos para crear la nueva etiqueta.
     * @return El {@code TagResponseDTO} de la nueva etiqueta creada.
     */
    TagResponseDTO create(TagRequestDTO tagRequestDTO);
}