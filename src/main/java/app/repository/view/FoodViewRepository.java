package app.repository.view;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.views.*;

public interface FoodViewRepository extends JpaRepository<FoodView, String> {

	@Query("select f from FoodView f where f.nombre =?1")
	FoodView findByNameAlimento(String nombre);
}
