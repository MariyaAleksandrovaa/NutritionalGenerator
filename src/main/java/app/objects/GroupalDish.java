package app.objects;

public class GroupalDish {

	public int id_dish;
	public String name_menu;
	public int id_menu;
	public String date_publish;

	public GroupalDish(int id_dish) {
		this.id_dish = id_dish;
	}

	public GroupalDish(int id_dish, String name_menu, String date_publish) {
		this.name_menu = name_menu;
		this.id_dish = id_dish;
		this.date_publish = date_publish;
	}

	public GroupalDish() {

	}

	public String getDate_publish() {
		return date_publish;
	}

	public void setDate_publish(String date_publish) {
		this.date_publish = date_publish;
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
