package app.views;

import java.io.Serializable;

public class UserId implements Serializable {

	private static final long serialVersionUID = 1L;

	public int user_id;
	public String rol;

	public UserId(int user_id, String rol) {
		this.user_id = user_id;
		this.rol = rol;
	}

	public UserId() {

	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

}
