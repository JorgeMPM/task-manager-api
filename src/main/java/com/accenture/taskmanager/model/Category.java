package com.accenture.taskmanager.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*Crea la entidad JPA Category en el paquete com.accenture.taskmanager.model.
Mapea la clase a la tabla category mediante @Entity y @Table(name = "category").
Añade los campos id de tipo Long, name de tipo String y description de tipo String.
El campo id debe utilizar @Id y @GeneratedValue(strategy = GenerationType.IDENTITY).
Añade una colección de taras mediante @OneToMany(mappedBy = "category", fetch = FetchType.LAZY).
No configures CascadeType ni añadas una JoinColumn en el lado inverso.
Incluye un constructor sin argumentos, los métodos necesarios y sus Javadoc en español, además del Javadoc de la clase.
No crees Repository, Service ni Controller.
—generado por IA*/
/**
 * Clase que representa una categoría de tareas en el sistema de gestión de tareas.
 * Esta clase está mapeada a la tabla "category" en la base de datos.   
 * 
 * @author jorge martin perez moreno
 * @version 1.0
 */
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Task> tasks;

    /**
     * Constructor sin argumentos de la clase Category.
     * Este constructor es necesario para que JPA pueda crear instancias de la entidad.
     */
    public Category() {
    }

    /**
     * Obtiene el identificador único de la categoría.
     * @return id de la categoría.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la categoría.
     * @param id el identificador de la categoría a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la categoría.
     * @return name de la categoría.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la categoría.
     * @param name el nombre de la categoría a establecer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la descripción de la categoría.
     * @return description de la categoría.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la categoría.
     * @param description la descripción de la categoría a establecer.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la lista de tareas asociadas a esta categoría.
     * @return lista de tareas asociadas a la categoría.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Establece la lista de tareas asociadas a esta categoría.
     * @param tasks la lista de tareas a asociar con la categoría.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}