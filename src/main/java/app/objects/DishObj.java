package app.objects;

import java.math.BigDecimal;
import java.util.Map;

public class DishObj {

	public Integer typeDish;
	public Map<String, BigDecimal> listFood;
	public String nombre_plato;
	public String descripcion;

	public DishObj(Integer typeDish, Map<String, BigDecimal> listFood, String nombre_plato, String descripcion) {
		this.typeDish = typeDish;
		this.listFood = listFood;
		this.nombre_plato = nombre_plato;
		this.descripcion = descripcion;
	}

	public DishObj() {

	}

	public Integer getTypeDish() {
		return typeDish;
	}

	public void setTypeDish(Integer typeDish) {
		this.typeDish = typeDish;
	}

	public Map<String, BigDecimal> getMapFood() {
		return listFood;
	}

	public void setMapFood(Map<String, BigDecimal> listFood) {
		this.listFood = listFood;
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
