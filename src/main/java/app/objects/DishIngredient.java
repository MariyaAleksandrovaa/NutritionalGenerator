package app.objects;

import java.math.BigDecimal;


public class DishIngredient {
	
	public Integer id_plato;
	public Integer id_ingrediente; 
	public String nombre_ingrediente;
	public BigDecimal cantidad;
	
	public DishIngredient(Integer id_plato, Integer id_ingrediente,String nombre_ingrediente,  BigDecimal cantidad) {
		this.id_plato = id_plato;
		this.id_ingrediente = id_ingrediente;
		this.nombre_ingrediente = nombre_ingrediente;
		this.cantidad = cantidad;
	}
	
	public DishIngredient() {
		
	}

	public String getNombre_ingrediente() {
		return nombre_ingrediente;
	}

	public void setNombre_ingrediente(String nombre_ingrediente) {
		this.nombre_ingrediente = nombre_ingrediente;
	}
	
	public Integer getId_plato() {
		return id_plato;
	}
	public void setId_plato(Integer id_plato) {
		this.id_plato = id_plato;
	}
	public Integer getId_ingrediente() {
		return id_ingrediente;
	}
	public void setId_ingrediente(Integer id_ingrediente) {
		this.id_ingrediente = id_ingrediente;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

}
