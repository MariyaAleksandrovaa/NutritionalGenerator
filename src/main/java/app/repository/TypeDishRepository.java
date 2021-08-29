package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.TypeDish;

public interface TypeDishRepository extends JpaRepository<TypeDish, Integer> {
	
	@Query("select td from TypeDish td where td.descripcion =?1")
	TypeDish findByTypeDish(String typeDish);

}
