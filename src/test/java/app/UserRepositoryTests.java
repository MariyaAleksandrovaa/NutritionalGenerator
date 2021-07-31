package app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import app.model.User;
import app.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCrateUser() {
		try {
			User user = new User();
			user.setUsername("silvia@gmail.com");
//			user.setId((long) 7);
			user.setPassword("$2a$10$yivrnNvLYDGzTimd1fdFp.Ti4IJCHdWT/nKK5Xs0aA5mWp6t228/G");
//			user.setFirstName("Manuel");
//			user.setLastName("García Lopez");

			User savedUser = repo.save(user);
			User existUser = entityManager.find(User.class, savedUser.getId());

			assertThat(existUser.getUsername()).isEqualTo(user.getUsername());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Test
	public void testFindUserByEmail() {

		String email = "gema@gmail.com";
		String username = "admin2";

//		User user = repo.getUserByUsername(username);
//		assertThat(user).isNotNull();
	}

}
