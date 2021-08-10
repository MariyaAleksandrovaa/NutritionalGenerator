package app.views;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "view_gestionplatos")
public class DishView {
	
	public String plato;
	@Id
	public String nombre_plato;
	public String descripcion;
	
	public String getPlato() {
		return plato;
	}
	public void setPlato(String plato) {
		this.plato = plato;
	}
	public String getNombre_plato() {
		return nombre_plato;
	}
	public void setNombre_plato(String nombre_plato) {
		this.nombre_plato = nombre_plato;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	
	
	
}
