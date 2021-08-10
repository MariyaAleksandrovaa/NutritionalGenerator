package app.views;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "view_gestionmenus")
public class MenuView {
	
	@Column()
	public Date fecha_creacion;
	
	@Id
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


	
	

}
