package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	@Query("select m from Menu m order by m.fecha_creacion desc")
	List<Menu> findAllOrderByDate();

}
