package app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import app.repository.view.DishViewRepository;
import app.repository.view.FoodViewRepository;
import app.repository.view.MenuViewRepository;
import app.views.DishView;
import app.views.FoodView;
import app.views.MenuView;


@Controller
public class ControllerMVC {
	
	@Autowired
	private MenuViewRepository menuRepo;
	
	@Autowired
	private FoodViewRepository foodRepoView;
	
	@Autowired
	private DishViewRepository dishRepo;

	@RequestMapping(value = {"/prueba", "/"}, method = RequestMethod.GET)
	public ModelAndView viewHomePage2() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("prueba");
		return model;
		
	}	
	
	@GetMapping("/admin")
	public String viewAdminPage(Model model) {
		List<MenuView> listMenus = menuRepo.findAll();
		List<FoodView> listFood = foodRepoView.findAll();
		List<DishView> listDish = dishRepo.findAll();

		model.addAttribute("listMenus", listMenus);		
		model.addAttribute("listFood", listFood);		
		model.addAttribute("listDish", listDish);		
		
		return "admin";
	}	
	
	@GetMapping("/editor")
	public String viewEditorPage() {

		return "editor";
	}	
	
	@RequestMapping(value = {"/user"}, method = RequestMethod.GET)
	public ModelAndView viewUserPage() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("user");
		return model;
	}	


}