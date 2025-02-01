package com.api.v3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootApplication
public class V3Application {

	public static void main(String[] args) {
		var modules = ApplicationModules.of(V3Application.class);
		modules.verify();
		SpringApplication.run(V3Application.class, args);
	}

}
