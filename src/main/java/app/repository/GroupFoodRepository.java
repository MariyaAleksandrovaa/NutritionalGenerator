package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.GroupFood;

public interface GroupFoodRepository extends JpaRepository<GroupFood, Integer> {

	@Query("select g from GroupFood g where g.grupo = ?1")
	public GroupFood findGroupByName(String nombre);
	
}
