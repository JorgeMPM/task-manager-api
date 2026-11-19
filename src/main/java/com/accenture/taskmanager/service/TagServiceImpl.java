package com.accenture.taskmanager.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.mapper.TagMapper;
import com.accenture.taskmanager.model.Tag;
import com.accenture.taskmanager.repository.TagRepository;

/**
 * Implementación del servicio para la gestión de etiquetas en el sistema de gestión de tareas.
 * Esta clase implementa la interfaz TagService y proporciona la lógica de negocio para las operaciones relacionadas con las etiquetas.
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Constructor de la clase TagServiceImpl.
     *
     * @param tagRepository El repositorio de etiquetas.
     * @param tagMapper El mapeador de etiquetas.
     */
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    /**
     * Devuelve una lista con todas las etiquetas.
     *
     * @return Una lista de {@code TagResponseDTO} con todas las etiquetas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponseDTO)
                .toList();
    }

    /**
     * Devuelve la etiqueta correspondiente al ID proporcionado.
     *
     * @param id El ID de la etiqueta a buscar.
     * @return El {@code TagResponseDTO} correspondiente a la etiqueta.
     * @throws NoSuchElementException Si no se encuentra la etiqueta.
     */
    @Override
    @Transactional(readOnly = true)
    public TagResponseDTO findById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toResponseDTO)
   
                .orElseThrow(() -> new NoSuchElementException("Tag con id " + id + " no encontrado"));
    }

    /**
     * Crea una nueva etiqueta a partir de los datos proporcionados.
     *
     * @param tagRequestDTO Los datos para crear la nueva etiqueta.
     * @return El {@code TagResponseDTO} de la nueva etiqueta creada.
     */
    @Override
    @Transactional
    public TagResponseDTO create(TagRequestDTO tagRequestDTO) {
        Tag tag = tagMapper.toEntity(tagRequestDTO);
        return tagMapper.toResponseDTO(tagRepository.save(tag));
    }

}   