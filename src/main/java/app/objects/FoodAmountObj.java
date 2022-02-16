package app.objects;

import java.math.BigDecimal;

public class FoodAmountObj {

	public String food;
	public BigDecimal amount;
	public Integer id_alimento;

	public FoodAmountObj(String food, BigDecimal amount) {
		this.food = food;
		this.amount = amount;
	}
	public FoodAmountObj(Integer id_alimento, BigDecimal amount) {
		this.id_alimento = id_alimento;
		this.amount = amount;
	}

	public FoodAmountObj() {

	}


	public Integer getId_alimento() {
		return id_alimento;
	}
	public void setId_alimento(Integer id_alimento) {
		this.id_alimento = id_alimento;
	}
	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
