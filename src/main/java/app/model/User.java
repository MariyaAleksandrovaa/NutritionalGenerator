package app.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 45, nullable = false)
	private String username;

	@Column(length = 200, nullable = false)
	private String password;

	@Column(length = 45, nullable = true)
	private String enable;

	@Column(length = 1, nullable = false)
	private boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();

		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private int idUsuario;
//
//	@Column(length = 20)
//	private String rol;
//
//	@Column(length = 50)
//	private String nombre;
//
//	@Column(length = 50)
//	private String apellidos;
//
//	@Column(length = 300)
//	private String email;
//
//	@Column(length = 300)
//	private String contraseña;
//
//	private int idEmpresa;

//	public int getIdUsuario() {
//		return idUsuario;
//	}
//
//	public void setIdUsuario(int idUsuario) {
//		this.idUsuario = idUsuario;
//	}
//
//	public String getRol() {
//		return rol;
//	}
//
//	public void setRol(String rol) {
//		this.rol = rol;
//	}
//
//	public String getNombre() {
//		return nombre;
//	}
//
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//
//	public String getApellidos() {
//		return apellidos;
//	}
//
//	public void setApellidos(String apellidos) {
//		this.apellidos = apellidos;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getContraseña() {
//		return contraseña;
//	}
//
//	public void setContraseña(String contraseña) {
//		this.contraseña = contraseña;
//	}
//
//	public int getIdEmpresa() {
//		return idEmpresa;
//	}
//
//	public void setIdEmpresa(int idEmpresa) {
//		this.idEmpresa = idEmpresa;
//	}

//	@Column(nullable=false, unique = true, length = 45)
//	private String email;
//	
//	@Column(nullable=false, unique = true, length = 45)
//	private String password;
//
//	@Column(nullable=true, unique = true, length = 25)
//	private String firstName;
//	
//	@Column(nullable=true, unique = true, length = 25)
//	private String lastName;

//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getFirstName() {
//		return firstName;
//	}
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//	
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}

}
