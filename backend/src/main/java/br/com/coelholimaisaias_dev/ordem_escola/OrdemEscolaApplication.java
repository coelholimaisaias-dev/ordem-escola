package br.com.coelholimaisaias_dev.ordem_escola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OrdemEscolaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdemEscolaApplication.class, args);
	}

}
