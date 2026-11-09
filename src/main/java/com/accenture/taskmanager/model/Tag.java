package com.accenture.taskmanager.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/*Crea la entidad JPA Tag en el paquete com.accenture.taskmanager.model.
Mapea la clase a la tabla tag mediante @Entity y @Table(name = "tag").
Añade los campos id de tipo Long y name de tipo String.
El campo id debe utilizar @Id y @GeneratedValue(strategy = GenerationType.IDENTITY).
Añade una colección Set<Task> tasks mediante @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY).
No añadas @JoinTable en esta clase porque Tag es el lado inverso de la relación.
No configures CascadeType.
Incluye un constructor sin argumentos, los métodos necesarios y sus Javadoc en español, además del Javadoc de la clase.
No crees Repository, Service ni Controller.
— generado por IA*/
/**
 * Clase que representa una etiqueta (tag) de tareas en el sistema de gestión de tareas.
 * Esta clase está mapeada a la tabla "tag" en la base de datos.   
 * 
 * @author jorge martin perez moreno
 * @version 1.0
 */
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Task> tasks;

    /**
     * Constructor sin argumentos de la clase Tag.
     * Este constructor es necesario para que JPA pueda crear instancias de la entidad.
     */
    public Tag() {
    }

    /**
     * Obtiene el identificador único de la etiqueta.
     * @return id de la etiqueta.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la etiqueta.
     * @param id el identificador de la etiqueta a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la etiqueta.
     * @return name de la etiqueta.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la etiqueta.
     * @param name el nombre de la etiqueta a establecer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el conjunto de tareas asociadas a esta etiqueta.
     * @return conjunto de tareas asociadas a la etiqueta.
     */
    public Set<Task> getTasks() {
        return tasks;
    }

    /**
     * Establece el conjunto de tareas asociadas a esta etiqueta.
     * @param tasks el conjunto de tareas a asociar con la etiqueta.
     */
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}