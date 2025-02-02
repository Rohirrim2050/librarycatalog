package com.librarycatalog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class LibraryCatalogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryCatalogApiApplication.class, args);
	}
}
