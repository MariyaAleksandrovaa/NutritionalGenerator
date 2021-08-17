package app.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import app.model.Dish;
import app.model.Empresa;
import app.repository.CompanyRepository;
import app.repository.DishRepository;
import app.repository.view.DishViewRepository;
import app.repository.view.FoodViewRepository;
import app.repository.view.MenuViewRepository;
import app.service.company.CompanyService;
import app.views.DishView;
import app.views.FoodView;
import app.views.MenuView;

@Controller
public class ControllerMVC {
	
	// View repositories

	@Autowired
	private MenuViewRepository menuViewRepo;

	@Autowired
	private FoodViewRepository foodViewRepo;

	@Autowired
	private DishViewRepository dishViewRepo;
	
	
	// Table repositories

	@Autowired
	private DishRepository dishRepo;
	
	@Autowired
	public CompanyService companyService;
	
	@Autowired
	public CompanyRepository companyRepo;
	
	
	
	@RequestMapping(value = { "/prueba", "/" }, method = RequestMethod.GET)
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
		List<Empresa> listCompanies = companyRepo.findAll();

		model.addAttribute("listFood", listFood);
		model.addAttribute("listCompanies", listCompanies);

		return "editor";
	}

	@GetMapping("/user")
	public String viewUserPage(Model model) {

		List<MenuView> listMenus = menuViewRepo.findAll();

		model.addAttribute("listMenus", listMenus);

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

	@RequestMapping(value="/editor/registrar_nueva_empresa", method = RequestMethod.GET)
	public ModelAndView viewRegistrarNuevaEmpresaPage() {
		
		ModelAndView model = new ModelAndView();
		Empresa empresa= new Empresa();
		
		model.addObject("empresa", empresa);
		model.setViewName("registrar_nueva_empresa");
		
		return model;
	}
	@RequestMapping(value="/editor/registrar_nueva_empresa/registrar_empresa_exito", method = RequestMethod.POST)
	public ModelAndView viewRegistrarNuevaEmpresa2Page(@Valid Empresa empresa, BindingResult bindingResult, ModelMap modelMap) {
		
		ModelAndView model = new ModelAndView();
		
		if(bindingResult.hasErrors()) {
			model.addObject("sucessMessage", "Por favor, corrige los errores");
			model.addObject("bindingResult", bindingResult);
			
		}else if(companyService.isCompanyAlreadyPresent(empresa)){
			model.addObject("sucessMessage", "Empresa ya existe!");
			
		}else {
			System.out.println("111111111111111");
			companyService.saveCompany(empresa);
			model.addObject("sucessMessage", "Empresa registrada con éxito!");
		}
		model.addObject("empresa", empresa);
		
		model.setViewName("registrar_empresa_exito");
		
		return model;
	}
	
//	@PostMapping("/editor/registrar_nueva_empresa/registrar_empresa_exito")
//	public String viewRegistrarNuevaEmpresaExitoPage(Empresa empresa) {
//		
////		companyService.save(empresa);
//
//		return "registrar_empresa_exito";
//	}


	@GetMapping("/editor/registrar_nuevo_usuario")
	public String viewRegistrarNuevoUsuarioPage() {

		return "registrar_nuevo_usuario";
	}

	@GetMapping("/editor/registrar_nuevo_local")
	public String viewRegistrarNuevoLocalPage() {

		return "registrar_nuevo_local";
	}
}