package app.objects;

public class User {

	public String username;
	public String password;
	public String name;
	public String surname;
	public String email;
	public String empresa;
	public String rol;

	public User(String username, String password, String name, String surname, String email, String empresa,
			String rol) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.empresa = empresa;
		this.rol = rol;
	}

	public User() {

	}

}
