package com.example.NutritionalGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class NutritionalGeneratorApplication implements CommandLineRunner{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(NutritionalGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		String sql = "INSERT INTO PRUEBA VALUES(?)";
//		int result = jdbcTemplate.update(sql, 6);
//		
//		if(result > 0) {
//			System.out.println("A new row has been inserted");
//		}
	}

}
