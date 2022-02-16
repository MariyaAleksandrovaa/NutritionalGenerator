package app.views;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "view_gestionplatos")
public class DishView {

	@Id
	public Integer id_plato;
	public String plato;
	public String nombre_plato;
	public String descripcion;
	public Integer id_empresa;
	public Timestamp fecha_creacion;

	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Timestamp fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Integer getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(Integer id_empresa) {
		this.id_empresa = id_empresa;
	}

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

	public Integer getId_plato() {
		return id_plato;
	}

	public void setId_plato(Integer id_plato) {
		this.id_plato = id_plato;
	}

}
