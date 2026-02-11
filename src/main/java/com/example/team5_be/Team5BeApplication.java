package com.example.team5_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Team5BeApplication {

	public static void main(String[] args) {
		Dotenv env = Dotenv.configure().ignoreIfMissing().load();
    	env.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(Team5BeApplication.class, args);
	}
}
