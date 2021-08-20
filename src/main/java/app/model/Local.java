package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locales")
public class Local {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id_local;
	
	public int id_empresa;
	
	public String nombre;
	public String direccion;

	public int getIdLocal() {
		return id_local;
	}

	public void setIdLocal(int idLocal) {
		this.id_local = idLocal;
	}

	public int getIdEmpresa() {
		return id_empresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.id_empresa = idEmpresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
