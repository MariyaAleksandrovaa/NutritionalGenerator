package app.objects;

public class MenuObj {

	public String name_menu;
	public int first_dish;
	public int second_dish;
	public Integer third_dish;

	public MenuObj(String name_menu, int first_dish, int second_dish, Integer third_dish) {
		this.name_menu = name_menu;
		this.first_dish = first_dish;
		this.second_dish = second_dish;
		this.third_dish = third_dish;
	}
	public MenuObj() {
		
	}

	public String getName_menu() {
		return name_menu;
	}

	public void setName_menu(String name_menu) {
		this.name_menu = name_menu;
	}

	public int getFirst_dish() {
		return first_dish;
	}

	public void setFirst_dish(int first_dish) {
		this.first_dish = first_dish;
	}

	public int getSecond_dish() {
		return second_dish;
	}

	public void setSecond_dish(int second_dish) {
		this.second_dish = second_dish;
	}

	public Integer getThird_dish() {
		return third_dish;
	}

	public void setThird_dish(Integer third_dish) {
		this.third_dish = third_dish;
	}

}
