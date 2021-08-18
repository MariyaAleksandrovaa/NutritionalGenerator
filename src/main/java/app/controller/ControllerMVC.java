package app.controller;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import app.model.Dish;
import app.model.Empresa;
import app.model.Food;
import app.model.GroupFood;
import app.repository.CompanyRepository;
import app.repository.DishRepository;
import app.repository.FoodRepository;
import app.repository.GroupFoodRepository;
import app.repository.view.DishViewRepository;
import app.repository.view.FoodViewRepository;
import app.repository.view.MenuViewRepository;
import app.service.company.CompanyNotfound;
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

	@Autowired
	public FoodRepository foodRepo;
	
	@Autowired
	public GroupFoodRepository groupFoodRepo;	
	
	

	@RequestMapping(value = { "/prueba", "/" }, method = RequestMethod.GET)
	public ModelAndView viewHomePage2() {

		ModelAndView model = new ModelAndView();
		model.setViewName("inicio_sesion");
		return model;

	}

	// NutriApp para administrador

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

	// NutriApp para editor

	@GetMapping("/editor")
	public String viewEditorPage(Model model) {

		List<FoodView> listFood = foodViewRepo.findAll();
		List<Empresa> listCompanies = companyRepo.findAll();

		model.addAttribute("listFood", listFood);
		model.addAttribute("listCompanies", listCompanies);

		return "editor";
	}

	// NutriApp para usuario normal

	@GetMapping("/user")
	public String viewUserPage(Model model) {

		List<MenuView> listMenus = menuViewRepo.findAll();

		model.addAttribute("listMenus", listMenus);

		return "user";
	}

	@GetMapping("/componentes")
	public String viewComponentsPage(Model model) {

		return "componentes";
	}

	@GetMapping("/alergenos")
	public String viewAlergenosPage(Model model) {

		return "alergenos";
	}

	// Funciones para ventana empresa (EDITOR)

	@RequestMapping(value = "/editor/registrar_nueva_empresa", method = RequestMethod.GET)
	public ModelAndView viewRegistrarNuevaEmpresaPage() {

		ModelAndView model = new ModelAndView();
		Empresa empresa = new Empresa();

		model.addObject("empresa", empresa);
		model.setViewName("registrar_nueva_empresa");

		return model;
	}

	@RequestMapping(value = "/editor/registrar_nueva_empresa/registrar_empresa_exito", method = RequestMethod.POST)
	public ModelAndView viewRegistrarNuevaEmpresa2Page(@Valid Empresa empresa, BindingResult bindingResult,
			ModelMap modelMap) {

		ModelAndView model = new ModelAndView();

		if (bindingResult.hasErrors()) {
			model.addObject("sucessMessage", "Por favor, corrige los errores");
			model.addObject("bindingResult", bindingResult);

		} else if (companyService.isCompanyAlreadyPresent(empresa)) {
			model.addObject("sucessMessage", "Empresa ya existe!");

		} else {
			companyService.saveCompany(empresa);
			model.addObject("sucessMessage", "Empresa registrada con éxito!");
		}
		model.addObject("empresa", empresa);

		model.setViewName("registrar_empresa_exito");

		return model;
	}

	@RequestMapping("/editor/edit/{id_empresa}")
	public ModelAndView editarEmpresa(@PathVariable("id_empresa") int id) {

		ModelAndView model = new ModelAndView("editar_empresa");

		try {

			model.addObject("empresa", companyService.get(id));

		} catch (CompanyNotfound e) {
			e.printStackTrace();
		}

		return model;
	}

	@PostMapping("/editor/guardar/{id_empresa}")
	public String guardarEmpresa(@PathVariable("id_empresa") int id, Empresa empresa) {

		empresa.setId_empresa(id);
		companyService.saveCompany(empresa);

		return "redirect:/editor";
	}


	@RequestMapping("/editor/delete/{id_empresa}")
	public String eliminarEmpresa(@PathVariable("id_empresa") int id) {

		try {
			companyRepo.delete(companyService.get(id));

		} catch (CompanyNotfound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/editor";
	}

	// Funciones para ventana usuario (EDITOR)

	@GetMapping("/editor/registrar_nuevo_usuario")
	public String viewRegistrarNuevoUsuarioPage() {

		return "registrar_nuevo_usuario";
	}

	// Funciones para ventana local (EDITOR)

	@GetMapping("/editor/registrar_nuevo_local")
	public String viewRegistrarNuevoLocalPage() {

		return "registrar_nuevo_local";
	}

	// Funciones para ventana alimento (EDITOR)

	@RequestMapping("/editor/editFood/{nombre}")
	public ModelAndView editarAlimento(@PathVariable("nombre") String nombre) {

		FoodView foodView = foodViewRepo.findByNameAlimento(nombre);
		List<GroupFood> listGroupFood = groupFoodRepo.findAll();
		
		ModelAndView model = new ModelAndView("editar_alimento");
		

		model.addObject("listGroupFood", listGroupFood);
		model.addObject("foodView", foodView);

		return model;
	}
	@PostMapping("/editor/guardarFood/{nombre}")
	public String guardarAlimento(@PathVariable("nombre") String nombre, FoodView foodView) {

		Food foodOld = foodRepo.findByNameAlimento(nombre);
		GroupFood groupfood = groupFoodRepo.findGroupById(Integer.parseInt(foodView.getGrupo()));
		
		foodOld.setIdGrupo(groupfood.getId_grupos_alimentos());
		foodOld.setNombre(foodView.getNombre());
		foodOld.setIngles(foodView.getIngles());
		foodOld.setEdible_portion(foodView.getEdible_portion());
		
		foodRepo.save(foodOld);

		return "redirect:/editor";
	}

	@RequestMapping("/editor/deleteFood/{nombre}")
	public String eliminarAlimento(@PathVariable("nombre") String nombre) {

		Food food = foodRepo.findByNameAlimento(nombre);

		foodRepo.delete(food);

		return "redirect:/editor";
	}

}