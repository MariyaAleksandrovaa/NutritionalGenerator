package app.objects;

import java.util.ArrayList;
import java.util.List;

public class MenuLocalObj {
	
	public String nombre_menu;
	public String date_publish;
	public List<Integer> list_id_local;
	
	public MenuLocalObj(String nombre_menu, ArrayList<Integer> list_id_local, String date_publish) {
		this.nombre_menu = nombre_menu;
		this.list_id_local = list_id_local;
		this.date_publish = date_publish;
	}
	
	public MenuLocalObj() {
		
	}

	public String getDate_publish() {
		return date_publish;
	}

	public void setDate_publish(String date_publish) {
		this.date_publish = date_publish;
	}

	
	public String getNombre_menu() {
		return nombre_menu;
	}

	public void setNombre_menu(String nombre_menu) {
		this.nombre_menu = nombre_menu;
	}

	public List<Integer> getList_id_local() {
		return list_id_local;
	}

	public void setList_id_local(List<Integer> list_id_local) {
		this.list_id_local = list_id_local;
	}



}
