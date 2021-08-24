package app.objects;

import java.math.BigDecimal;

public class FoodAmountObj {

	public String food;
	public BigDecimal amount;

	public FoodAmountObj(String food, BigDecimal amount) {
		this.food = food;
		this.amount = amount;
	}

	public FoodAmountObj() {

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
