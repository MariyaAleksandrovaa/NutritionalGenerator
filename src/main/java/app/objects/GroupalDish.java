package app.objects;

public class GroupalDish {

	public int id_dish;
	public String name_menu;
	public int id_menu;

	public GroupalDish(int id_dish) {
		this.id_dish = id_dish;
	}

	public GroupalDish(int id_dish, String name_menu) {
		this.name_menu = name_menu;
		this.id_dish = id_dish;
	}

	public GroupalDish() {

	}

	public int getId_menu() {
		return id_menu;
	}

	public void setId_menu(int id_menu) {
		this.id_menu = id_menu;
	}

	public String getName_menu() {
		return name_menu;
	}

	public void setName_menu(String name_menu) {
		this.name_menu = name_menu;
	}

	public int getId_dish() {
		return id_dish;
	}

	public void setId_dish(int id_dish) {
		this.id_dish = id_dish;
	}

}
