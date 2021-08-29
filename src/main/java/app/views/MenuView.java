package app.views;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "view_gestionmenus")
public class MenuView {

	@Id
	public int id_menu;

	@Column()
	public Date fecha_creacion;
	
	@Column(length = 100)
	public String nombre_menu;
	
	@Column(length = 100)
	public String descripcion;
	
	
	
	public Date getFechaCreacion() {
		return fecha_creacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fecha_creacion = fechaCreacion;
	}

	public String getNombreMenu() {
		return nombre_menu;
	}

	public void setNombreMenu(String nombreMenu) {
		this.nombre_menu = nombreMenu;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public int getId_menu() {
		return id_menu;
	}

	public void setId_menu(int id_menu) {
		this.id_menu = id_menu;
	}


	
	

}
