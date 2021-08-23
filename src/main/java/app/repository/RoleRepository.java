package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	@Query("select r from Role r where r.name = ?1")
	public Role findByNameRole(String rol);

}
