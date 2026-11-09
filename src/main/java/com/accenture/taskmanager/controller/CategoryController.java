package com.accenture.taskmanager.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.accenture.taskmanager.dto.ErrorResponse;

/*
 Crea la clase CategoryController anotada con @RestController y @RequestMapping("/api/v1/categories").
 Declara un campo private final CategoryService categoryService y un constructor que lo reciba y lo asigne.
 Los métodos devolverán ResponseEntity y delegarán en categoryService.
 Añade Javadoc en español en la clase y el constructor. — generado por IA
*/
/* Añade @Tag con name "Categorías" y una descripción en español. — generado por IA */
/**
 * Controlador REST que expone las operaciones CRUD de categorías en
 * /api/v1/categories.
 * Los errores los gestiona el GlobalExceptionHandler.
 */
@Tag(name = "Categorías", description = "Operaciones relacionadas con la gestión de categorías")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructor de la clase CategoryController.
     *
     * @param categoryService el servicio de categorías a utilizar
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /*
     * Crea el método findAll con GetMapping que reciba un Pageable anotado con
     * PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
     * y devuelva ResponseEntity.ok(categoryService.findAll(pageable)).
     * Añade Javadoc en español. — generado por IA
     */
    /*
     * Añade @Operation con summary en español y @ApiResponses: 200 sin content
     * y 400 con ErrorResponse si el campo de ordenación no es válido. — generado por IA
     */
    /*
     * Añade @ParameterObject al parámetro Pageable para que springdoc muestre
     * page, size y sort como parámetros de consulta. — generado por IA
     */
    /**
     * Método para obtener todas las categorías con paginación.
     *
     * @param pageable el objeto Pageable que contiene la información de paginación
     *                 y ordenamiento
     * @return ResponseEntity con la lista de categorías encontradas
     */
    @Operation(summary = "Obtiene todas las categorías con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Campo de ordenación no válido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PagedResponseDTO<CategoryResponseDTO>> findAll(
                @ParameterObject @PageableDefault(size = 10, sort = "name", direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }

    /*
     * Crea el método findById con GetMapping("/{id}") que reciba PathVariable Long
     * id
     * y devuelva ResponseEntity.ok(categoryService.findById(id)).
     * Añade Javadoc en español. — generado por IA
     */
    /*
     * Añade @Operation con summary en español y @ApiResponses:
     * 200 sin content y 404 con ErrorResponse. — generado por IA
     */
    /**
     * Método para obtener una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar
     * @return ResponseEntity con la categoría encontrada o un estado de error si no
     *         se encuentra
     */
    @Operation(summary = "Obtiene una categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    /*
     * Crea el método create con PostMapping que reciba RequestBody
     * CategoryRequestDTO dto
     * y devuelva
     * ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto)).
     * Añade Javadoc en español. — generado por IA
     */
    /*
     * Añade @Operation con summary en español y @ApiResponses: 201 sin content. —
     * generado por IA
     */
    /**
     * Método para crear una nueva categoría.
     *
     * @param categoryRequestDTO el DTO con los datos de la nueva categoría
     * @return ResponseEntity con la categoría creada y un estado de creación
     *         exitosa
     */
    @Operation(summary = "Crea una nueva categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada correctamente")
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryRequestDTO));
    }

    /*
     * Crea el método update con PutMapping("/{id}") que reciba PathVariable Long id
     * y RequestBody CategoryRequestDTO dto
     * y devuelva ResponseEntity.ok(categoryService.update(id, dto)).
     * Añade Javadoc en español. — generado por IA
     */
    /*
     * Añade @Operation con summary en español y @ApiResponses:
     * 200 sin content y 404 con ErrorResponse. — generado por IA
     */
    /**
     * Método para actualizar una categoría existente.
     *
     * @param id                 el ID de la categoría a actualizar
     * @param categoryRequestDTO el DTO con los datos actualizados de la categoría
     * @return ResponseEntity con la categoría actualizada o un estado de error si
     *         no se encuentra
     */
    @Operation(summary = "Actualiza una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id,
            @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryRequestDTO));
    }

    /*
     * Crea el método delete con DeleteMapping("/{id}") que reciba PathVariable Long
     * id,
     * llame a categoryService.delete(id) y devuelva
     * ResponseEntity.noContent().build() con tipo ResponseEntity<Void>.
     * Añade Javadoc en español. — generado por IA
     */
    /*
     * Añade @Operation con summary en español y @ApiResponses:
     * 204 sin content y 404 con ErrorResponse. — generado por IA
     */
    /**
     * Método para eliminar una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar
     * @return ResponseEntity con un estado de no contenido si la eliminación es
     *         exitosa
     */
    @Operation(summary = "Elimina una categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}