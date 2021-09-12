package app.objects;

public class LocalEnable {

	public int id_local;

	public int id_empresa;

	public String nombre;
	public String direccion;

	public boolean enable;

	public LocalEnable(String nombre, String direccion, int id_local, int id_empresa, boolean enable) {

		this.nombre = nombre;
		this.direccion = direccion;
		this.id_local = id_local;
		this.id_empresa = id_empresa;
		this.enable = enable;
	}

	public LocalEnable() {

	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getIdLocal() {
		return id_local;
	}

	public void setIdLocal(int idLocal) {
		this.id_local = idLocal;
	}

	public int getIdEmpresa() {
		return id_empresa;
	}

	public void setIdEmpresa(int id_empresa) {
		this.id_empresa = id_empresa;
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
