package app.views;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "view_gestionalimentos")
public class FoodView {

	public String grupo;

	@Id
	public int id_alimento;
	public String nombre;
	public String ingles;
	public BigDecimal edible_portion;

	
	
	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIngles() {
		return ingles;
	}

	public void setIngles(String ingles) {
		this.ingles = ingles;
	}

	public BigDecimal getEdible_portion() {
		return edible_portion;
	}

	public void setEdible_portion(BigDecimal edible_portion) {
		this.edible_portion = edible_portion;
	}

}
