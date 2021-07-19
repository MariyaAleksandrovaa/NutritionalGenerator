package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

//import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import app.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("select u from User u where u.username = :username")
	public User getUserByUsername(@Param("username") String username);
		
	
	
//JpaRepository<User, Long> {

//	@Query("select u from User u where u.email =?1")
//	User findByEmail(String email);
		
	
}
