package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.Food;
import app.model.User;
import app.repository.FoodRepository;
import app.repository.UserRepository;

@Controller
public class ControllerMVC {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FoodRepository foodRepo;

	@GetMapping({ "/", "/login" })
	public String login() {
		return "index2";
	}

	@GetMapping("/menu")
	public String menu() {
		return "menu";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@GetMapping("/editor")
	public String editor() {
		return "editor";
	}

	@GetMapping("/user")
	public String user() {
		return "user";
	}

	@GetMapping("/register")
	public String viewRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}

	@PostMapping("/process_register")
	public String processRegistration(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());

		user.setPassword(encodedPassword);
		userRepo.save(user);

		return "register_success";
	}

	@RequestMapping(value = "/list_users", method = RequestMethod.GET)
	public String viewUsersList(Model model) {

		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);

		return "users";
	}

	@RequestMapping(value = "/list_food", method = RequestMethod.GET)
	public String viewFoodList(Model model) {

		List<Food> listFood = foodRepo.findAll();
		model.addAttribute("listFood", listFood);

		return "food";
	}

}
