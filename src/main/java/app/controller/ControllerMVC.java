package app.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import app.Application;
import app.model.Dish;
import app.model.Empresa;
import app.model.Food;
import app.model.GroupFood;
import app.model.Local;
import app.model.Role;
import app.model.TypeDish;
import app.model.User;
import app.objects.DishObj;
import app.objects.FoodAmountObj;
import app.objects.ResetPwdObj;
import app.parametrizedObjects.AlergensFood;
import app.parametrizedObjects.ComponentsFood;
import app.repository.CompanyRepository;
import app.repository.DishRepository;
import app.repository.FoodRepository;
import app.repository.GroupFoodRepository;
import app.repository.LocalRepository;
//import app.repository.LocalRepository;
import app.repository.RoleRepository;
import app.repository.TypeDishRepository;
import app.repository.UserRepository;
import app.repository.view.DishViewRepository;
import app.repository.view.FoodViewRepository;
import app.repository.view.LocalViewRepository;
import app.repository.view.MenuViewRepository;
import app.repository.view.UserViewRepository;
import app.service.company.CompanyNotfound;
import app.service.company.CompanyService;
import app.views.DishView;
import app.views.FoodView;
import app.views.LocalView;
import app.views.MenuView;
import app.views.UserView;

@Controller
public class ControllerMVC {

	// View repositories

	@Autowired
	private MenuViewRepository menuViewRepo;

	@Autowired
	private FoodViewRepository foodViewRepo;

	@Autowired
	private DishViewRepository dishViewRepo;

	@Autowired
	private UserViewRepository userViewRepo;

	@Autowired
	private LocalViewRepository localViewRepo;

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

	@Autowired
	public UserRepository userRepo;

	@Autowired
	public RoleRepository roleRepo;

	@Autowired
	private LocalRepository localRepo;

	@Autowired
	private TypeDishRepository typeDishRepo;

	public String password;

	public DishObj dishObj;

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
		List<DishView> listDish = dishViewRepo.findAll();

		List<Dish> dishes = dishRepo.findAll();

		model.addAttribute("listMenus", listMenus);
		model.addAttribute("listDish", listDish);
		model.addAttribute("dishes", dishes);

		return "admin";
	}

	// NutriApp para editor

	@GetMapping("/editor")
	public String viewEditorPage(Model model) {

		List<FoodView> listFood = foodViewRepo.findAll();
		List<Empresa> listCompanies = companyRepo.findAll();
		List<UserView> listUsers = userViewRepo.findAll();
		List<LocalView> listLocals = localViewRepo.findAll();

		model.addAttribute("listFood", listFood);
		model.addAttribute("listCompanies", listCompanies);
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("listLocals", listLocals);

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

	// Funciones para ventana local (EDITOR)

	@GetMapping("/editor/registrar_nuevo_local")
	public ModelAndView viewRegistrarNuevoLocalPage() {

		ModelAndView model = new ModelAndView("registrar_nuevo_local");

		List<Empresa> listCompanies = companyRepo.findAll();
		Local local = new Local();
		int select = -1;

		model.addObject("listCompanies", listCompanies);
		model.addObject("local", local);
		model.addObject("select", select);

		return model;
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
		GroupFood groupfood = groupFoodRepo.findGroupByName(foodView.getGrupo());

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

	@RequestMapping("/editor/AlergenosFood/{nombre}")
	public ModelAndView mostrarAlergenos(@PathVariable("nombre") String nombre) {

		ModelAndView model = new ModelAndView("alergenos");

		Food food = foodRepo.findByNameAlimento(nombre);

		List<AlergensFood> listaAlergenos = obtenerBDalergenosAlimento(food.getIdAlimento());

		model.addObject("listaAlergenos", listaAlergenos);

		return model;
	}

	public List<AlergensFood> obtenerBDalergenosAlimento(int idAlimento) {

		List<AlergensFood> listaAlergenos = new ArrayList<AlergensFood>();

		try {

			Statement st = Application.con.createStatement();
			ResultSet rs = st.executeQuery("SELECT t.alergeno, t.descripcion, a.tieneAlergeno\r\n"
					+ "FROM tiposalergenos as t right join alimentos_tiposalergenos as a on t.idTiposAlergenos = a.idTiposAlergenos\r\n"
					+ "WHERE idAlimentos=" + idAlimento + " and\r\n" + "tieneAlergeno='si';\r\n" + "");

			while (rs.next()) {
				String nombreAlergeno = rs.getString(1);
				String descripcionAlergeno = rs.getString(2);

				AlergensFood alergeno = new AlergensFood(nombreAlergeno, descripcionAlergeno);
				listaAlergenos.add(alergeno);
			}
			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaAlergenos;
	}

	@RequestMapping("/editor/ComponentesFood/{nombre}")
	public ModelAndView mostrarComponentes(@PathVariable("nombre") String nombre) {

		ModelAndView model = new ModelAndView("componentes");

		Food food = foodRepo.findByNameAlimento(nombre);

		List<ComponentsFood> listaComponentes = obtenerBDcomponentesAlimento(food.getIdAlimento());

		model.addObject("listaComponentes", listaComponentes);

		return model;
	}

	public List<ComponentsFood> obtenerBDcomponentesAlimento(int idAlimento) {

		List<ComponentsFood> listaComponentes = new ArrayList<ComponentsFood>();

		try {

			Statement st = Application.con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT g.nombre, ac.c_ori_name, ac.best_location, ac.v_unit, ac.mu_descripcion  \r\n"
							+ "FROM nutri_db.alimentos_componentesquimicos as ac left join componentesquimicos as c on ac.c_ori_name = c.c_ori_name \r\n"
							+ "left join gruposcomponentes as g on c.componentgroup_id = g.idGruposComponentes\r\n"
							+ "where idAlimento = " + idAlimento + "\r\n" + "and ac.best_location > 0\r\n"
							+ "order by best_location desc;");

			while (rs.next()) {
				String nombreComponente = rs.getString(1);
				String descripcionComponente = rs.getString(2);
				Float valor = rs.getFloat(3);
				String unidad = rs.getString(4);
				String descripcion = rs.getString(5);

				ComponentsFood componente = new ComponentsFood(nombreComponente, descripcionComponente, valor, unidad,
						descripcion);
				listaComponentes.add(componente);
			}
			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaComponentes;
	}

	// Funciones para ventana usuario (EDITOR)

	@RequestMapping("/editor/registrar_nuevo_usuario/exito")
	public ModelAndView viewRegistrarNuevoUsuario(User user, BindingResult bindingResult, ModelMap modelMap) {

		ModelAndView model = new ModelAndView("registrar_usuario_exito");

		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);

		if (user.getIdEmpresa() == -1) {
			user.setIdEmpresa(null);
		}

		userRepo.save(user);

		return model;
	}

	@GetMapping("/editor/registrar_nuevo_usuario")
	public ModelAndView viewRegistrarNuevoUsuarioPage() {

		ModelAndView model = new ModelAndView("registrar_nuevo_usuario");

		List<Empresa> listCompanies = companyRepo.findAll();
		List<Role> listRoles = roleRepo.findAll();
		String companyMsg = "Escoge una empresa";
		int select = -1;

		User usuario = new User();
//		usuario.setIdEmpresa(9);

		Role role = new Role();

		model.addObject("usuario", usuario);
		model.addObject("listCompanies", listCompanies);
		model.addObject("listRoles", listRoles);
		model.addObject("companyMsg", companyMsg);
		model.addObject("select", select);
		model.addObject("role", role);

		return model;
	}

	@RequestMapping("/editor/deleteUser/{user_id}")
	public String eliminarUsuario(@PathVariable("user_id") int user_id) {

		Optional<User> user = userRepo.findById(user_id);
		userRepo.delete(user.get());

		return "redirect:/editor";
	}

	@RequestMapping("/editor/editUser/{user_id}")
	public ModelAndView editarUsuario(@PathVariable("user_id") int user_id) {

		ModelAndView model = new ModelAndView("editar_usuario");

		Optional<UserView> user = userViewRepo.findById(user_id);
		List<Role> listRoles = roleRepo.findAll();
		List<Empresa> listCompanies = companyRepo.findAll();

		password = user.get().getPassword();

		model.addObject("listRoles", listRoles);
		model.addObject("listCompanies", listCompanies);
		model.addObject("user", user.get());
		model.addObject("empresa", user.get().getNombre());
		model.addObject("select", -1);

		return model;
	}

	@PostMapping("/editor/guardarUser/{user_id}/{rol}")
	public String guardarUsuario(@PathVariable("user_id") int user_id, @PathVariable("rol") String role,
			UserView userView) {

		Optional<User> user = userRepo.findById(userView.getUser_id());
		User usr = user.get();

		if (!password.equals(userView.getPassword())) {

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(userView.getPassword());

			usr.setPassword(encodedPassword);

		}
		Empresa empresa = companyRepo.findByNameCompany(userView.getNombre());
		if (empresa != null) {
			usr.setIdEmpresa(empresa.getId_empresa());
		}

		usr.setEmail(userView.getEmail());

		usr.setName(userView.getName());
		usr.setUsername(userView.getUsername());
		usr.setSurname(userView.getSurname());

		try {

			Statement st = Application.con.createStatement();

			int rol = 0;

			if (userView.getRol().equals("USER")) {
				rol = 1;

			} else if (userView.getRol().equals("ADMIN")) {
				rol = 2;

			} else if (userView.getRol().equals("EDITOR")) {
				rol = 3;

			}
			st.execute("update users_roles set role_id = " + rol + " where user_id = " + usr.getId() + ";");

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		userRepo.save(usr);

		return "redirect:/editor";
	}

	@RequestMapping("/editor/deleteLocal/{local}")
	public String eliminarLocal(@PathVariable("local") String local) {

		try {

			Statement st = Application.con.createStatement();
			st.execute("delete from locales where nombre='" + local + "';");

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "redirect:/editor";
	}

	@RequestMapping("/editor/registrar_nuevo_local/exito")
	public ModelAndView viewRegistrarNuevoLocal(Local local, BindingResult bindingResult, ModelMap modelMap) {

		ModelAndView model = new ModelAndView("registrar_local_exito");

		localRepo.save(local);

		return model;
	}

	@RequestMapping("/editor/editLocal/{id_local}")
	public ModelAndView editarLocal(@PathVariable("id_local") int id_local) {

		ModelAndView model = new ModelAndView("editar_local");
		try {

			Statement st = Application.con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from view_gestionlocales where id_local=" + id_local + ";");

			while (rs.next()) {

				LocalView localView = new LocalView(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				model.addObject("localView", localView);
			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<Empresa> listCompanies = companyRepo.findAll();
		int select = -1;

		model.addObject("listCompanies", listCompanies);
		model.addObject("select", select);

		return model;
	}

	@RequestMapping("/editor/editar_local_exito/{id_local}")
	public String guardarLocal(LocalView localObj, @PathVariable("id_local") int id_local) {

		try {
			Statement st = Application.con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from locales where id_local=" + id_local + ";");

			while (rs.next()) {
				String nombre = rs.getString(1);
				String direccion = rs.getString(2);
				int id_localObj = rs.getInt(3);
				int id_empresa = rs.getInt(4);

				Local local = new Local(nombre, direccion, id_localObj, id_empresa);
				Empresa empresa = companyRepo.findByNameCompany(localObj.getEmpresa());

				local.setDireccion(localObj.getDireccion());
				local.setNombre(localObj.getLocal());
				local.setIdEmpresa(empresa.getId_empresa());

				localRepo.save(local);

				rs.close();
				st.close();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/editor";
	}

	@RequestMapping("/restablecer_contraseña")
	public ModelAndView restablecerContraseña() {

		ModelAndView model = new ModelAndView("/restablecer_contraseña");
		ResetPwdObj resetPwdObj = new ResetPwdObj();

		model.addObject("resetPwdObj", resetPwdObj);

		return model;
	}

	@RequestMapping("/restablecer_contraseña/exito")
	public ModelAndView restablecerContraseñaExito(ResetPwdObj resetPwdObj) {

		ModelAndView model = new ModelAndView("inicio_sesion");

		User user = userRepo.findByUsername(resetPwdObj.getUsername());

		if (user != null) {
			if (resetPwdObj.getPwd().equals(resetPwdObj.getResetPwd())) {

				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				user.setPassword(encoder.encode(resetPwdObj.getPwd()));
				userRepo.save(user);

			}
		}

		return model;
	}

	List<Food> listFoodDish = new ArrayList<Food>();

	@RequestMapping(value = "/admin/crear_nuevo_plato", method = RequestMethod.GET)
	public ModelAndView crearNuevoPlato() {

		ModelAndView model = new ModelAndView("crear_nuevo_plato");
		dishObj = new DishObj();

		List<TypeDish> listTypeDishes = typeDishRepo.findAll();

		model.addObject("listTypeDishes", listTypeDishes);
		model.addObject("dishObj", dishObj);

		return model;
	}

	@PostMapping("/admin/crear_nuevo_plato/ingredientes")
	public ModelAndView escogerIngredientes(DishObj dishObj) {

		ModelAndView model = new ModelAndView("ingredientes");

		this.dishObj = dishObj;

		List<FoodView> listFood = foodViewRepo.findAll();

		model.addObject("listFood", listFood);

		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/ingredientes/{id_alimento}")
	public ModelAndView añadirIngrediente(@PathVariable("id_alimento") int id_alimento) {

		ModelAndView model = new ModelAndView("opciones_ingrediente");

		Food food = foodRepo.findByIdAlimento(id_alimento);
		listFoodDish.add(food);
		dishObj.setMapFood(ingredientes);

		List<Food> listFood = foodRepo.findAll();

		FoodAmountObj foodAmountObj = new FoodAmountObj();
		foodAmountObj.setFood(food.getNombre());

		model.addObject("listFood", listFood);
		model.addObject("foodAmountObj", foodAmountObj);

		return model;
	}

	public Map<String, BigDecimal> ingredientes = new HashMap<String, BigDecimal>();

	@PostMapping("/admin/crear_nuevo_plato/ingrediente")
	public ModelAndView guardarIngrediente(FoodAmountObj foodAmountObj) {

		ModelAndView model = new ModelAndView("ingredientes");
		ingredientes.put(foodAmountObj.getFood(), foodAmountObj.getAmount());
		List<FoodView> listFood = foodViewRepo.findAll();

		model.addObject("listFood", listFood);

		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/ingrediente/terminarPlato")
	public ModelAndView terminarPlato() {

		ModelAndView model = new ModelAndView("terminar_plato");

		model.addObject("listIngredientes", ingredientes);

		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/ingrediente/terminarPlatoExito")
	public ModelAndView terminarPlatoExito() {

		ModelAndView model = new ModelAndView("admin");

		Dish dish = new Dish();
		dish.setNombre_plato(dishObj.getNombre_plato());
		dish.setDescripcion(dishObj.getDescripcion());

		dish.setId_tipo_platos(Integer.parseInt(dishObj.getTypeDish()));

		dishRepo.save(dish);

		for (var ingrediente : dishObj.getMapFood().entrySet()) {

			try {
				Statement st = Application.con.createStatement();
				st.executeUpdate("insert into platos_alimentos (idPlato, idAlimento, cantidad) values ("
						+ dish.getId_plato() + ", " + foodRepo.findByNameAlimento(ingrediente.getKey()).getIdAlimento()
						+ "," + ingrediente.getValue() + ");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return model;
	}
//	/admin/deleteDish

	@RequestMapping("/admin/deleteDish/{id_plato}")
	public String eliminarPlato(@PathVariable("id_plato") int id_plato) {

		dishRepo.deleteById(id_plato);

		return "redirect:/admin";
	}

}