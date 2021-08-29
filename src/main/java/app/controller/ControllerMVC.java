package app.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import app.model.ComponentsDishTable;
import app.model.Dish;
import app.model.Empresa;
import app.model.Food;
import app.model.GroupFood;
import app.model.Local;
import app.model.Menu;
import app.model.Role;
import app.model.TypeDish;
import app.model.User;
import app.objects.DishObj;
import app.objects.FoodAmountObj;
import app.objects.GroupUnitObj;
import app.objects.GroupalDish;
import app.objects.MenuObj;
import app.objects.ResetPwdObj;
import app.parametrizedObjects.AlergensFood;
import app.parametrizedObjects.ComponentsFood;
import app.repository.CompanyRepository;
import app.repository.DishRepository;
import app.repository.FoodRepository;
import app.repository.GroupFoodRepository;
import app.repository.LocalRepository;
import app.repository.MenuRepository;
//import app.repository.LocalRepository;
import app.repository.RoleRepository;
import app.repository.TypeDishRepository;
import app.repository.UserRepository;
import app.repository.view.DishViewRepository;
import app.repository.view.FoodViewRepository;
import app.repository.view.LocalViewRepository;
import app.repository.view.MenuViewRepository;
import app.repository.view.UserViewRepository;
import app.service.CustomUserDetails;
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

	@Autowired
	private MenuRepository menuRepo;

	public String password;

	public DishObj dishObj;

	public User obtenerUsuario() {
		return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	}

	@RequestMapping(value = { "/prueba", "/" }, method = RequestMethod.GET)
	public ModelAndView viewHomePage2() {

		ModelAndView model = new ModelAndView();
		model.setViewName("inicio_sesion");
		return model;

	}

	// NutriApp para administrador

	@GetMapping("/admin")
	public String viewAdminPage(Model model) {

		int id_empresa = obtenerUsuario().getIdEmpresa();

		List<DishView> listDish = obtenerPlatosUsuario(id_empresa);
		List<Menu> listMenus = obtenerMenusUsuario(id_empresa);

		model.addAttribute("listMenus", listMenus);
		model.addAttribute("listDish", listDish);

		return "admin";
	}

	private List<DishView> obtenerPlatosUsuario(int id_empresa) {

		List<DishView> listDishAll = dishViewRepo.findAll();
		List<DishView> listDish = new ArrayList<DishView>();

		for (int i = 0; i < listDishAll.size(); i++) {
			DishView dishView = listDishAll.get(i);

			if (dishView.getId_empresa() == id_empresa) {
				listDish.add(dishView);
			}
		}

		return listDish;
	}

	private List<Menu> obtenerMenusUsuario(int id_empresa) {

		List<Menu> listMenusAll = menuRepo.findAll();

		List<Menu> listMenu = new ArrayList<Menu>();

		for (int i = 0; i < listMenusAll.size(); i++) {
			Menu menuView = listMenusAll.get(i);

			if (menuView.getId_empresa() == id_empresa) {
				listMenu.add(menuView);
			}
		}

		return listMenu;

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
		ingredientes = new HashMap<String, BigDecimal>();

		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/terminarPlatoExito")
	public String terminarPlatoExito() {

		Dish dish = new Dish();
		dish.setNombre_plato(dishObj.getNombre_plato());
		dish.setDescripcion(dishObj.getDescripcion());

		dish.setId_empresa(obtenerUsuario().getIdEmpresa());

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

		return "redirect:/admin";
	}

	@RequestMapping("/admin/deleteDish/{id_plato}")
	public String eliminarPlato(@PathVariable("id_plato") int id_plato) {

		dishRepo.deleteById(id_plato);

		return "redirect:/admin";
	}

	@RequestMapping("/admin/editDish/{id_plato}")
	public ModelAndView editarPlato(@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("editar_plato");

		Dish dish = dishRepo.findById(id_plato).get();
		List<TypeDish> listTypeDish = typeDishRepo.findAll();

		model.addObject("dish", dish);
		model.addObject("listTypeDish", listTypeDish);

		return model;
	}

	@PostMapping("/admin/saveDish/{id_plato}")
	public String guardarPlato(Dish dish) {

		dish.setId_empresa(obtenerUsuario().getIdEmpresa());
		dishRepo.save(dish);

		return "redirect:/admin";
	}

	@RequestMapping("/admin/ComponentesDish/{id_plato}")
	public ModelAndView mostrarComponentesPlato(@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("componentes_plato");

		List<ComponentsDishTable> listComponentsDish = obtenerBDcomponentesPlato(id_plato);

		model.addObject("listComponentsDish", listComponentsDish);

		return model;
	}

	public List<ComponentsDishTable> obtenerBDcomponentesPlato(int id_plato) {

		List<ComponentsDishTable> listComponentDishTable = new ArrayList<ComponentsDishTable>();

		try {

			// 1º Buscar todos los ingredientes del plato y sus cantidades

			Map<Integer, BigDecimal> mapIngredientAmount = new HashMap<Integer, BigDecimal>();

			Statement st = Application.con.createStatement();
			ResultSet rs = st.executeQuery("select idAlimento, cantidad\r\n" + "from platos_alimentos\r\n"
					+ "where idPlato = " + id_plato + ";");

			while (rs.next()) {
				mapIngredientAmount.put(rs.getInt(1), rs.getBigDecimal(2));

			}
			rs.close();
			st.close();

			// 2º Encontrar los componentes quimicos de cada alimento

			// Mapa que contiene los ingredientes del plato
			Map<Integer, List<ComponentsFood>> mapComponentsDish = new HashMap<Integer, List<ComponentsFood>>();

			Statement st2 = Application.con.createStatement();

			// Lista que almacena los componentes de todos los alimentos del plato
			List<ComponentsFood> listaComponentes = new ArrayList<ComponentsFood>();

			// Almacenar el grupo y unidad de los componentess
			Map<String, GroupUnitObj> mapComponentUnit = new HashMap<String, GroupUnitObj>();

			for (var ingrediente : mapIngredientAmount.entrySet()) {
				ResultSet rs2 = st2.executeQuery(
						"SELECT g.nombre, ac.c_ori_name, ac.best_location, ac.v_unit, ac.mu_descripcion  \r\n"
								+ "FROM nutri_db.alimentos_componentesquimicos as ac left join componentesquimicos as c on ac.c_ori_name = c.c_ori_name \r\n"
								+ "left join gruposcomponentes as g on c.componentgroup_id = g.idGruposComponentes\r\n"
								+ "where idAlimento = " + ingrediente.getKey() + "\r\n" + "and ac.best_location > 0\r\n"
								+ "order by best_location desc;");

				while (rs2.next()) {
					String nombreComponente = rs2.getString(1);
					String descripcionComponente = rs2.getString(2);
					Float valor = rs2.getFloat(3);
					String unidad = rs2.getString(4);
					String descripcion = rs2.getString(5);

					ComponentsFood componente = new ComponentsFood(nombreComponente, descripcionComponente, valor,
							unidad, descripcion);

					listaComponentes.add(componente);

					GroupUnitObj groupUnitObj = new GroupUnitObj(nombreComponente, unidad);
					mapComponentUnit.put(descripcionComponente, groupUnitObj);

				}
				mapComponentsDish.put(ingrediente.getKey(), listaComponentes);

				// Reinicio lista de componentes para que los componentes de cada alimento los
				// almacene en una entrada distinta del mapa

				listaComponentes = new ArrayList<ComponentsFood>();

				rs2.close();

			}
			st2.close();

			Map<String, Float> mapComponentsDish2 = new HashMap<String, Float>();

			for (var ingrediente : mapComponentsDish.entrySet()) {

				ingrediente.getKey();
				List<ComponentsFood> listaCompon = ingrediente.getValue();

				for (int i = 0; i < listaCompon.size(); i++) {
					if (mapComponentsDish2.get(listaCompon.get(i).getC_ori_name()) == null) {

						Float amount = listaCompon.get(i).getBest_location();

						String proportion = listaCompon.get(i).getDescripcion();
						Float propor = null;
						switch (proportion) {
						case "por 100 g de porción comestible":
							propor = amount / 100;
							break;
						case "por Kg de parte comestible":
							propor = amount / 1000;
							break;
						case "por 100 g de peso en seco":
							propor = amount / 100;
							break;
						case "por ml de volumen del alimento":
							propor = amount;
							break;

						}
						BigDecimal quantity = mapIngredientAmount.get(ingrediente.getKey());
						mapComponentsDish2.put(listaCompon.get(i).getC_ori_name(), propor * quantity.floatValue());

					} else {
						Float value = mapComponentsDish2.get(listaCompon.get(i).getC_ori_name());
						Float amount = listaCompon.get(i).getBest_location();

						String proportion = listaCompon.get(i).getDescripcion();
						Float propor = null;
						switch (proportion) {
						case "por 100 g de porción comestible":
							propor = amount / 100;
							break;
						case "por Kg de parte comestible":
							propor = amount / 1000;
							break;
						case "por 100 g de peso en seco":
							propor = amount / 100;
							break;
						case "por ml de volumen del alimento":
							propor = amount;
							break;

						}
						BigDecimal quantity = mapIngredientAmount.get(ingrediente.getKey());
						Float sum = value + propor * quantity.floatValue();
						mapComponentsDish2.replace(listaCompon.get(i).getC_ori_name(), sum);

					}
				}

			}

			for (var componente : mapComponentsDish2.entrySet()) {

				ComponentsDishTable componentDishTable = new ComponentsDishTable();

				componentDishTable.setNameComponent(componente.getKey());
				componentDishTable.setAmount(componente.getValue());

				GroupUnitObj groupUnitObj = mapComponentUnit.get(componente.getKey());

				componentDishTable.setGroupComponent(groupUnitObj.getGroup());
				componentDishTable.setUnit(groupUnitObj.getUnit());

				listComponentDishTable.add(componentDishTable);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listComponentDishTable;
	}

	@RequestMapping("/admin/AlergenosDish/{id_plato}")
	public ModelAndView mostrarAlergenosPlato(@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("alergenos_plato");

		Map<String, String> mapAlergensDish = obtenerAlergenosPlato(id_plato);

//		4º Enviar alérgenos a la página			
		model.addObject("mapAlergensDish", mapAlergensDish);

		return model;
	}

	public Map<String, String> obtenerAlergenosPlato(int id_plato) {

		Map<String, String> mapAlergensDish = null;

		try {
//			1º Obtener los ids de los alimentos que tiene un plato

			Statement st = Application.con.createStatement();

			ResultSet rs = st.executeQuery(
					"select idAlimento\r\n" + "from platos_alimentos\r\n" + "where idPlato =" + id_plato + ";");

			mapAlergensDish = new HashMap<String, String>();
			while (rs.next()) {

//				2º Por cada id obtener sus alérgenos

				List<AlergensFood> listAlergensFood = obtenerBDalergenosAlimento(rs.getInt(1));

				for (AlergensFood alergensFood : listAlergensFood) {

//					3º Almacenar en un mapa los alérgenos

					mapAlergensDish.put(alergensFood.getAlergeno(), alergensFood.getDescripcion());

				}

			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapAlergensDish;
	}

	@RequestMapping("/admin/crear_menu_individual")
	public ModelAndView crearMenuIndividual() {

		ModelAndView model = new ModelAndView("crear_menu_individual");

		List<Dish> listDish = dishRepo.findAll();
		int id_company = obtenerUsuario().getIdEmpresa();
		int select = 0;

		List<Dish> listDishCompany = new ArrayList<Dish>();

		for (int i = 0; i < listDish.size(); i++) {
			Integer id_empresa = listDish.get(i).getId_empresa();
			if (id_empresa != null && id_empresa == id_company) {
				listDishCompany.add(listDish.get(i));
			}
		}

		MenuObj menuObj = new MenuObj();

		model.addObject("listDishCompany", listDishCompany);
		model.addObject("menuObj", menuObj);
		model.addObject("select", select);

		return model;
	}

	@RequestMapping("/admin/crear_menu_individual/guardar")
	public String guardarMenuIndividual(MenuObj menuObj) {

		List<Integer> listDishes = new ArrayList<Integer>();

		listDishes.add(menuObj.getFirst_dish());
		listDishes.add(menuObj.getSecond_dish());
		listDishes.add(menuObj.getThird_dish());

		Menu menu = new Menu();
		String description = "Menú individual";
		menu.setDescripcion(description);
		menu.setNombre_menu(menuObj.getName_menu());

		int id_company = obtenerUsuario().getIdEmpresa();
		menu.setId_empresa(id_company);

		menu.setFecha_creacion(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

		Menu menuDb = menuRepo.save(menu);

		if (menuObj.getThird_dish() == 0) {
			menuObj.setThird_dish(null);
		}

		try {
			Statement st = Application.con.createStatement();

			for (int i = 0; i < listDishes.size(); i++) {
				if (listDishes.get(i) != 0) {
					String query = "insert into menus_platos values(" + menuDb.getId_menu() + "," + listDishes.get(i)
							+ ",'" + menuObj.getName_menu() + "','" + description + "'," + id_company + ");";
					st.execute(query);
				}
			}

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/admin";
	}

	@RequestMapping("/admin/crear_menu_grupal")
	public ModelAndView crearMenuGrupal() {

		ModelAndView model = new ModelAndView("crear_menu_grupal");

		listDishesGroupalMenu = new ArrayList<Integer>();

		Menu menu = new Menu();
		model.addObject("menu", menu);

		return model;
	}

	private Menu menu_grupal;

	@PostMapping("/admin/crear_menu_grupal/guardar")
	public String crearMenuGrupalGuardar(Menu menu) {
		
		

		menu.setDescripcion("Menú grupal");
		menu.setFecha_creacion(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		menu.setId_empresa(obtenerUsuario().getIdEmpresa());

		menu_grupal = menuRepo.save(menu);


		return "redirect:/admin/crear_menu_grupal/platos";
	}

	@GetMapping("/admin/crear_menu_grupal/platos")
	public ModelAndView escogerPlatosMenuGrupal() {
		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal");
		
		GroupalDish groupalDish = new GroupalDish();
		int select = 0;
		List<Dish> listDish = dishRepo.findAll();
		model.addObject("groupalDish", groupalDish);
		model.addObject("select", select);
		model.addObject("listDish", listDish);
		
		
//		/admin/escoger_platos_menu_grupal/{id_menu}
		return model;
	}

	public List<Integer> listDishesGroupalMenu;

	@RequestMapping("/admin/crear_menu_grupal/guardarPlato")
	public String añadirPlatoMenuGrupal(GroupalDish groupalDish) {

		int id_menu = menu_grupal.getId_menu();
		String nombre_menu = menu_grupal.getNombre_menu();
		String descripcion_menu = menu_grupal.getDescripcion();
		int empresa_menu = menu_grupal.getId_empresa();

		listDishesGroupalMenu.add(groupalDish.getId_dish());

		try {
			Statement st = Application.con.createStatement();

			String query = "insert into menus_platos values(" + id_menu + "," + groupalDish.getId_dish() + ",'"
					+ nombre_menu + "','" + descripcion_menu + "'," + empresa_menu + ");";
			st.execute(query);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/admin/crear_menu_grupal/platos";
	}


	@RequestMapping("/admin/crear_nuevo_plato/terminarMenu")
	public ModelAndView terminarMenuGrupal() {

		ModelAndView model = new ModelAndView("mostrar_platos_menu");
		Map<Integer, String> mapDishesMenu = new HashMap<Integer, String>();

		for (int i = 0; i < listDishesGroupalMenu.size(); i++) {
			int id_dish = listDishesGroupalMenu.get(i);
			mapDishesMenu.put(id_dish, dishRepo.findById(id_dish).get().getNombre_plato());
		}

		model.addObject("mapDishesMenu", mapDishesMenu);

		return model;
	}

	@RequestMapping("/admin/crear_menu_grupal/terminar")
	public String terminarMenuGrupalExito() {

		return "redirect:/admin";
	}

	@RequestMapping("/admin/deleteMenu/{id_menu}")
	public String eliminarMenu(@PathVariable("id_menu") int id_menu) {

		menuRepo.delete(menuRepo.findById(id_menu).get());

		return "redirect:/admin";
	}

	@GetMapping("/admin/AlergenosMenu/{id_menu}")
	public String obtenerAlergenosMenuIndividual(@PathVariable("id_menu") int id_menu) {

		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();

		if (type_menu.equals("Menú individual")) {
			return "redirect:/admin/alergenos_menu_individual/{id_menu}";

		} else if (type_menu.equals("Menú grupal")) {
			return "redirect:/admin/escoger_platos_menu_grupal/{id_menu}";
		}
		return null;

	}

	@GetMapping("/admin/alergenos_menu_individual/{id_menu}")
	public ModelAndView alergenosMenusIndividuales(@PathVariable("id_menu") int id_menu) {
		ModelAndView model = new ModelAndView("alergenos_menu");
		Map<String, String> mapAlergensMenu = obtenerAlergenosMenu(id_menu);
		model.addObject("mapAlergensMenu", mapAlergensMenu);

		return model;
	}

	@RequestMapping("/admin/alergenos_menu_colectivo")
	public ModelAndView alergenosMenusColectivos(MenuObj menuObj) {
		ModelAndView model = new ModelAndView("alergenos_menu");

		Map<String, String> mapAlergensMenu = new HashMap<String, String>();

		if (menuObj.getFirst_dish() != 0) {
			Map<String, String> mapFirstDish = obtenerAlergenosPlato(menuObj.getFirst_dish());

			for (var alergen : mapFirstDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		} 
		if (menuObj.getSecond_dish() != 0) {
			Map<String, String> mapSecondDish = obtenerAlergenosPlato(menuObj.getSecond_dish());

			for (var alergen : mapSecondDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		} 
		if (menuObj.getThird_dish() != 0) {
			Map<String, String> mapThirdDish = obtenerAlergenosPlato(menuObj.getThird_dish());

			for (var alergen : mapThirdDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}

		model.addObject("mapAlergensMenu", mapAlergensMenu);

		return model;
	}

//	Permite escoger los platos del menú que se van a consumir para proceder a calcular los alérgeno del mismo
	@GetMapping("/admin/escoger_platos_menu_grupal/{id_menu}")
	public ModelAndView escogerPlatosMenuGrupalAlergenos(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_alergenos");

		Map<Integer, String> mapaPlatosTipo1Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo2Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo3Menu = new HashMap<Integer, String>();

		try {

			Statement st = Application.con.createStatement();

			for (int i = 1; i <= 3; i++) {
				ResultSet rs = st.executeQuery("select mp.idPlato, p.nombre_plato\r\n"
						+ "from menus_platos as mp right join platos as p on mp.idPlato = p.id_plato \r\n"
						+ "where mp.idMenu = " + id_menu + " and mp.descripcion = 'Menú grupal' and p.id_tipo_platos = "
						+ i + ";");

				while (rs.next()) {
					if (i == 1) {
						mapaPlatosTipo1Menu.put(rs.getInt(1), rs.getString(2));
					} else if (i == 2) {
						mapaPlatosTipo2Menu.put(rs.getInt(1), rs.getString(2));
					} else if (i == 3) {
						Integer id_plato = rs.getInt(1);
						String nombre_plato = rs.getString(2);

						if (id_plato != null) {
							mapaPlatosTipo3Menu.put(id_plato, nombre_plato);
						}

					}

				}

				rs.close();
			}

			st.close();

			model.addObject("mapaPlatosTipo1Menu", mapaPlatosTipo1Menu);
			model.addObject("mapaPlatosTipo2Menu", mapaPlatosTipo2Menu);
			model.addObject("mapaPlatosTipo3Menu", mapaPlatosTipo3Menu);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		MenuObj menuObj = new MenuObj();
		model.addObject("menuObj", menuObj);

		int select = 0;
		model.addObject("select", select);

		return model;
	}

	// Obtiene los alergenos de los platos de un menú indicando el id del menu (Para
	// menús individuales donde los platos del menú son los mismos que los que va a
	// tomar el cliente )
	public Map<String, String> obtenerAlergenosMenu(int id_menu) {
		Map<String, String> mapAlergensMenu = new HashMap<String, String>();

		try {

			Statement st = Application.con.createStatement();

			ResultSet rs = st.executeQuery("select idPlato from menus_platos where idMenu = " + id_menu + ";");

			while (rs.next()) {

				int idPlato = rs.getInt(1);

				Map<String, String> mapAlergensDish = obtenerAlergenosPlato(idPlato);

				for (var alergenDish : mapAlergensDish.entrySet()) {

					mapAlergensMenu.put(alergenDish.getKey(), alergenDish.getValue());
				}

			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapAlergensMenu;

	}

	@RequestMapping("/admin/ComponentesMenu/{id_menu}")
	public String mostrarComponentesMenu(@PathVariable("id_menu") int id_menu) {

		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();

		if (type_menu.equals("Menú individual")) {
			return "redirect:/admin/componentes_menu_individual/{id_menu}";

		} else if (type_menu.equals("Menu grupal")) {
			return "redirect:/admin/escoger_platos_menu_grupal/{id_menu}";
		}

		return null;
	}

	@GetMapping("/admin/componentes_menu_individual/{id_menu}")
	public ModelAndView componentesMenuIndividual(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("componentes_plato");

		List<ComponentsDishTable> listComponentsDish = obtenerComponentesMenu(id_menu);
		model.addObject("listComponentsDish", listComponentsDish);

		return model;
	}

	public List<ComponentsDishTable> obtenerComponentesMenu(int id_menu) {
		List<ComponentsDishTable> listListComponentsDish = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> listComponentsDish;
		Map<String, Float> mapComponentsMenu = new HashMap<String, Float>();

		try {

			Statement st = Application.con.createStatement();

			ResultSet rs = st.executeQuery("select idPlato from menus_platos where idMenu = " + id_menu + ";");

			while (rs.next()) {

				int idPlato = rs.getInt(1);
				if (idPlato != 0) {
					// He obtenido los componentes que tiene un plato
					listComponentsDish = obtenerBDcomponentesPlato(idPlato);

					for (int i = 0; i < listComponentsDish.size(); i++) {
						// Cuando no existe entrada en mapa para ese componente
						if (mapComponentsMenu.get(listComponentsDish.get(i).getNameComponent()) == null) {
							mapComponentsMenu.put(listComponentsDish.get(i).getNameComponent(),
									listComponentsDish.get(i).getAmount());

						} else {
							float oldAmount = mapComponentsMenu.get(listComponentsDish.get(i).getNameComponent());
							float newAmount = listComponentsDish.get(i).getAmount();
							mapComponentsMenu.replace(listComponentsDish.get(i).getNameComponent(),
									oldAmount + newAmount);
						}
					}
					listListComponentsDish.addAll(listComponentsDish);
				}

			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Mantengo lista con componentes sin repetir
//		List<ComponentsDishTable> listFinalComponents = new ArrayList<>();
		// Recorro las listas de listas
		for (int i = 0; i < listListComponentsDish.size(); i++) {

			String component = listListComponentsDish.get(i).getNameComponent();

			for (int j = 0; j < listListComponentsDish.size(); j++) {
				if (i != j) {
					String component2 = listListComponentsDish.get(j).getNameComponent();
					if (component.equals(component2)) {
						listListComponentsDish.remove(j);

					}
				}

			}
		}
		for (int i = 0; i < listListComponentsDish.size(); i++) {
			listListComponentsDish.get(i)
					.setAmount(mapComponentsMenu.get(listListComponentsDish.get(i).getNameComponent()));
		}

		return listListComponentsDish;

	}

}