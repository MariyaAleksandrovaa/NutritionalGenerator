package com.example.NutritionalGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	private Long id;
	
	@Column(nullable=false, unique = true, length = 45)
	private String email;
	
	@Column(nullable=false, unique = true, length = 45)
	private String password;

	@Column(nullable=false, unique = true, length = 25)
	private String first_name;
	
	@Column(nullable=false, unique = true, length = 25)
	private String last_name;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return first_name;
	}
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}
	public String getLastName() {
		return last_name;
	}
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
