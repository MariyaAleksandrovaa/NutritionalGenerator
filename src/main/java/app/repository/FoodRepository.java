package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Food;

public interface FoodRepository extends JpaRepository<Food, Integer> {

	@Query("select f from Food f where f.idAlimento =?1")
	Food findByIdAlimento(int idAlimento);
}
