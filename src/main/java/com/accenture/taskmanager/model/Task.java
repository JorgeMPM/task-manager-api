package com.accenture.taskmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/*Esta clase Task representa una tarea en el sistema de gestión de tareas, 
con atributos para almacenar la información de cada tarea. 
Debe contener los siguientes atributos:
- id: identificador único de la tarea.
- title: título de la tarea.
- description: descripción de la tarea.
- status: estado de la tarea.
- priority: prioridad de la tarea.
- dueDate: fecha de vencimiento de la tarea.
—generado por IA*/

/*Convertir esta clase en una entidad JPA mapeada a la tabla task, con id autogenerado por MySQL y cada atributo asociado a su columna. Incluye las anotaciones @Entity,@Table("task"),@Id,@GeneratedValue(IDENTITY) para ello.

Añade a los campos que ya hay en la clase Task el campo createdAt de tipo LocalDateTime y categoryId de tipo Long, que admita null.

Añade @Column a los campos cuyo nombre no coincide con la columna de SQL (due_date, created_at, category_id). 
—generado por IA
*/

/**
 * Clase Task, que representa una tarea en el sistema de gestión de tareas.
 * 
 * @author jorge martin perez moreno
 * @version 1.0
 */
@Entity
@Table(name = "task")
public class Task {

    //Propiedades de la clase Task.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    //Añade @Enumerated(EnumType.STRING) a los campos status y priority de la clase Task para que los valores de los enums TaskStatus y TaskPriority se almacenen como texto en la base de datos. —generado por IA
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /*
    Añade a la entidad Task el campo Category category como una nueva relación JPA.
    Utiliza @ManyToOne(fetch = FetchType.LAZY) y @JoinColumn(name = "category_id").
    Si existe el atributo categoryId de tipo Long mapeado a la columna category_id, sustitúyelo por esta relación para evitar mapear dos veces la misma columna.
    Conserva el resto de los campos y métodos existentes de Task.
    Añade Javadoc en español para el nuevo atributo.
    No cambies los endpoints ni la lógica del Controller o del Service.
    —generado por IA */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /*Añade a la entidad Task el campo Set<Tag> tags para representar la relación muchos a muchos con Tag.
    Utiliza @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}).
    Configura @JoinTable(name = "task_tag") como lado propietario de la relación.
    Utiliza joinColumns para la columna que referencia a Task e inverseJoinColumns para la columna que referencia a Tag.
    Comprueba los nombres reales de las columnas de task_tag antes de configurarlas.
    No utilices CascadeType.ALL ni CascadeType.REMOVE.
    Conserva el resto de los campos y métodos existentes de Task y añade Javadoc en español para el nuevo atributo.
    No cambies los endpoints ni la lógica del Controller o del Service.
    —generado por IA*/
    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();  

    /**
     * Constructor vacio de la clase Task.
     */
    public Task(){
    }

    /**
     * Constructor con parametros de la clase Task.
     * @param id identificador único de la tarea.
     * @param title título de la tarea.
     * @param description descripción de la tarea.
     * @param status estado de la tarea.
     * @param priority prioridad de la tarea.
     * @param dueDate fecha de vencimiento de la tarea.
     * @param createdAt fecha de creación de la tarea.
     */
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority, LocalDate  dueDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    //──────────────────────── Metodos Getter y Setter ────────────────────────
    //Metodos publicos para acceder y modificar los atributos privados.

    /**
     * Metodo que devuelve el valor del atributo id.
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo id.
     * @param id identificador único de la tarea.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Metodo que devuelve el valor del atributo title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo title.
     * @param title título de la tarea.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Metodo que devuelve el valor del atributo description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo description.
     * @param description descripción de la tarea.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Metodo que devuelve el valor del atributo status.
     * @return status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo status.
     * @param status estado de la tarea.
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Metodo que devuelve el valor del atributo priority.
     * @return priority
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo priority.
     * @param priority prioridad de la tarea.
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /**
     * Metodo que devuelve el valor del atributo dueDate.
     * @return dueDate
     */
    public LocalDate  getDueDate() {
        return dueDate;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo dueDate.
     * @param dueDate fecha de vencimiento de la tarea.
     */
    public void setDueDate(LocalDate  dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Metodo que devuelve el valor del atributo createdAt.
     * @return createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo createdAt.
     * @param createdAt
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Metodo que devuelve el valor del atributo category.
     * @return category 
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo category.
     * @param category categoría de la tarea.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Metodo que devuelve el valor del atributo tags.
     * @return tags
     */ 
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Metodo que asigna o modifica el valor del atributo tags.
     * @param tags conjunto de etiquetas asociadas a la tarea.
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /*
    Añade un método con la anotación @PrePersist que se ejecute antes de insertar una nueva tarea en la base de datos.
    Si el atributo createdAt es null, asígnale la fecha y hora actuales mediante LocalDateTime.now().
    Si createdAt ya tiene un valor, consérvalo sin modificarlo.
    El método debe llamarse onCreate() y tener visibilidad protected.
    No modifiques el resto de la clase.
    —generado por IA
    */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(ZoneId.systemDefault());
        }
    }
}
