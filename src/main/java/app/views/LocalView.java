package app.views;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "view_gestionlocales")
public class LocalView {

	@Id
	public int id_local;
	public String local;
	public String empresa;
	public String direccion;

	public LocalView(int id_local, String local, String direccion, String empresa) {
		this.id_local = id_local;
		this.local = local;
		this.empresa = empresa;
		this.direccion = direccion;

	}

	public LocalView() {
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public int getId_local() {
		return id_local;
	}

	public void setId_local(int id_local) {
		this.id_local = id_local;
	}

}
