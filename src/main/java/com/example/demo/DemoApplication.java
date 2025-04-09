package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	//ajuda a execução de comandos SQL simples
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	//estamos a verificar a conexao à base de dados (CommandLineRunner é um tipo especial do Bean)
	@Bean
	public CommandLineRunner testarConexao(){
		return args -> {
			try {
				jdbcTemplate.execute("SELECT 1");
				System.out.println("Está ligada à base de dados");
			} catch (Exception e) {
				System.out.println("Não foi possivel ligar à base de dados");
				return;
			}

			// Listar todos os utilizadores
			System.out.println("Utilizadores na base de dados:");
			userRepository.findAll().forEach(user ->
				System.out.println(user.getId() + " - " + user.getName() + " - " + user.getEmail())
			);
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer () {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("http://localhost:3000").allowedMethods("GET", "POST"/*, "PUT"*/, "DELETE", "PATCH");
			}
		};
	}

}
