package app.repository.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.views.LocalView;

public interface LocalViewRepository extends JpaRepository<LocalView, String> {

	@Query("select l from LocalView l where l.local =?1")
	LocalView findByNameLocal(String nombre);

}
