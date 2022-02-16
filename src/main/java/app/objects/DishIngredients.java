package app.objects;

import java.math.BigDecimal;

public class DishIngredients {
	
	public String ingrediente;
	public Integer id_ingrediente;
	public BigDecimal cantidad;
	
	public DishIngredients(String ingrediente, Integer id_ingrediente, BigDecimal cantidad) {
		this.ingrediente = ingrediente;
		this.id_ingrediente = id_ingrediente;
		this.cantidad = cantidad;
	}
	
	public DishIngredients() {
		
	}
	
	
	public String getIngrediente() {
		return ingrediente;
	}
	public void setIngrediente(String ingrediente) {
		this.ingrediente = ingrediente;
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
