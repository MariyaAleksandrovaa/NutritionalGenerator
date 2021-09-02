package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Local;

public interface LocalRepository extends JpaRepository<Local, Integer> {
	@Query("select l from Local l where l.nombre = ?1")
	public Local findByNameLocal(String local);
	
	@Query("select l from Local l where l.id_empresa = ?1")
	public List<Local> findByIdEmpresa(int id_empresa);
}
