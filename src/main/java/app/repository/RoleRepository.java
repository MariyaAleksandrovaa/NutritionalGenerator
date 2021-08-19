package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
