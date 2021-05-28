package com.example.NutritionalGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCrateUser() {
		try {
			User user = new User();
			user.setEmail("alex@gmail.com");
//			user.setId(new Long(1));
			user.setPassword("alex");
			user.setFirstName("Alex");
			user.setLastName("García");

			
			User savedUser = repo.save(user);
			User existUser = entityManager.find(User.class, savedUser.getId());
			
			assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}

		
	}
	
	
}
