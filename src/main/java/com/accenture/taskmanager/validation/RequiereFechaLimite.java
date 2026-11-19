package com.accenture.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequiereFechaLimite es una anotación de validación personalizada que se aplica a nivel de clase.
 * Esta anotación asegura que las tareas con prioridad HIGH tengan una fecha límite establecida.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiereFechaLimiteValidator.class)
public @interface RequiereFechaLimite {
    String message() default "Las tareas con prioridad HIGH deben tener fecha límite";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}