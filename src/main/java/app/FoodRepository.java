package app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FoodRepository extends JpaRepository<Food, Integer> {

	@Query("select f from Food f where f.idAlimento =?1")
	Food findByIdAlimento(int idAlimento);
}
