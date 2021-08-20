package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Local;

public interface LocalRepository extends JpaRepository<Local, Integer> {
	@Query("select l from Local l where l.nombre = ?1")
	public Local findByNameLocal(String local);
}
