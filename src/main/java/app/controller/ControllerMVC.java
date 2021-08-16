package app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import app.model.Dish;
import app.repository.DishRepository;
import app.repository.view.DishViewRepository;
import app.repository.view.FoodViewRepository;
import app.repository.view.MenuViewRepository;
import app.views.DishView;
import app.views.FoodView;
import app.views.MenuView;


@Controller
public class ControllerMVC {
	
	@Autowired
	private MenuViewRepository menuViewRepo;
	
	@Autowired
	private FoodViewRepository foodViewRepo;
	
	@Autowired
	private DishViewRepository dishViewRepo;
	
	@Autowired
	private DishRepository dishRepo;

	@RequestMapping(value = {"/prueba", "/"}, method = RequestMethod.GET)
	public ModelAndView viewHomePage2() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("prueba");
		return model;
		
	}	
	
	@GetMapping("/admin")
	public String viewAdminPage(Model model) {
		
		List<MenuView> listMenus = menuViewRepo.findAll();
		List<FoodView> listFood = foodViewRepo.findAll();
		List<DishView> listDish = dishViewRepo.findAll();
		
		List<Dish> dishes = dishRepo.findAll();

		model.addAttribute("listMenus", listMenus);		
		model.addAttribute("listFood", listFood);		
		model.addAttribute("listDish", listDish);	
		model.addAttribute("dishes", dishes);
		
		return "admin";
	}	
	
	@GetMapping("/editor")
	public String viewEditorPage(Model model) {
		
		List<FoodView> listFood = foodViewRepo.findAll();
			
		model.addAttribute("listFood", listFood);	
		
		return "editor";
	}	
	
	@GetMapping("/user")
	public String viewUserPage(Model model) {
		
		List<MenuView> listMenus = menuViewRepo.findAll();
//		List<FoodView> listFood = foodViewRepo.findAll();
//		List<DishView> listDish = dishViewRepo.findAll();
//		
//		List<Dish> dishes = dishRepo.findAll();

		model.addAttribute("listMenus", listMenus);		
//		model.addAttribute("listFood", listFood);		
//		model.addAttribute("listDish", listDish);	
//		model.addAttribute("dishes", dishes);
		
		return "user";
	}	
	
	
//	@RequestMapping(value = {"/user"}, method = RequestMethod.GET)
//	public ModelAndView viewUserPage() {
//		
//		ModelAndView model = new ModelAndView();
//		model.setViewName("user");
//		return model;
//	}	
	
	
	
	@GetMapping("/componentes")
	public String viewComponentsPage(Model model) {
		
		return "componentes";
	}	
	
	@GetMapping("/alergenos")
	public String viewAlergenosPage(Model model) {
		
		return "alergenos";
	}	


}