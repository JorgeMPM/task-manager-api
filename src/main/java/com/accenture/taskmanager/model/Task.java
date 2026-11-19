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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

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

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(ZoneId.systemDefault());
        }
    }
}
