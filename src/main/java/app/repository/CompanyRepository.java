package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.model.Empresa;

@Repository
public interface CompanyRepository extends JpaRepository<Empresa, Integer> {
	

}
