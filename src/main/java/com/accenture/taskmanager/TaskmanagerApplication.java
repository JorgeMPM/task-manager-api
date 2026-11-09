package com.accenture.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase TaskmanagerApplication principal desde donde arrancamos el servidor.
 * 
 * @author jorge martin perez moreno
 * @version 1.0
 */
@SpringBootApplication
public class TaskmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}

}
