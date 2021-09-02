package app.objects;

public class MenuLocalObj {
	
	public String nombre_menu;
	public Integer id_local;
	
	public MenuLocalObj(String nombre_menu, Integer id_local ) {
		this.nombre_menu = nombre_menu;
		this.id_local = id_local;
	}
	
	public MenuLocalObj() {
		
	}
	
	public String getNombre_menu() {
		return nombre_menu;
	}

	public void setNombre_menu(String nombre_menu) {
		this.nombre_menu = nombre_menu;
	}

	public Integer getId_local() {
		return id_local;
	}

	public void setId_local(Integer id_local) {
		this.id_local = id_local;
	}



}
