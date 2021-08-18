package app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Food;

public interface FoodRepository extends JpaRepository<Food, Integer> {

	@Query("select f from Food f where f.id_alimento =?1")
	Food findByIdAlimento(int id_alimento);
	
	@Query("select f from Food f where f.nombre =?1")
	Food findByNameAlimento(String nombre);


}
