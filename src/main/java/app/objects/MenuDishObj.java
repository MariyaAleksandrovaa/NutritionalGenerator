package app.objects;

public class MenuDishObj {
	public String nombre_menu;

	public Integer id_plato;
	public Integer id_menu;
	
	public MenuDishObj(String nombre_menu, Integer id_plato, Integer id_menu) {
		this.nombre_menu = nombre_menu;
		this.id_plato = id_plato;
		this.id_menu  =id_menu;
	}
	public MenuDishObj() {
		
	}
	public String getNombre_menu() {
		return nombre_menu;
	}
	public void setNombre_menu(String nombre_menu) {
		this.nombre_menu = nombre_menu;
	}
	public Integer getId_plato() {
		return id_plato;
	}
	public void setId_plato(Integer id_plato) {
		this.id_plato = id_plato;
	}
	public Integer getId_menu() {
		return id_menu;
	}
	public void setId_menu(Integer id_menu) {
		this.id_menu = id_menu;
	}

}
