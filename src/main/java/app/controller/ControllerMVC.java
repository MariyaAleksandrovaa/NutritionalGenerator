package app.controller;

import java.math.BigDecimal;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import app.objects.DishIngredient;
import app.objects.DishIngredients;
import app.objects.DishObj;
import app.objects.FoodAmountObj;
import app.objects.GroupUnitObj;
import app.objects.GroupalDish;
import app.objects.LocalEnable;
import app.objects.MenuLocalObj;
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
import app.repository.view.UserViewRepository;
import app.service.CustomUserDetails;
import app.service.company.CompanyNotfound;
import app.service.company.CompanyService;
import app.views.DishView;
import app.views.FoodView;
import app.views.LocalView;
import app.views.UserView;

@Controller
public class ControllerMVC {

	// View repositories

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

	@RequestMapping(value = { "/volver_inicio" }, method = RequestMethod.POST)
	public String viewInicio() {

		return "redirect:/";
	}

	// NutriApp para administrador

	@GetMapping("/admin")
	public String viewAdminPage(Model model) {

		int id_empresa = obtenerUsuario().getIdEmpresa();

		List<DishView> listDish = obtenerPlatosUsuario(id_empresa);
		List<Menu> listMenus = obtenerMenusUsuario(id_empresa);

		model.addAttribute("listMenus", listMenus);
		model.addAttribute("listDish", listDish);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addAttribute("company", company);
		}

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

		List<Menu> listMenusAll = menuRepo.findAllOrderByDate();

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addAttribute("company", company);
		}

		return "editor";
	}

	// NutriApp para usuario normal

	@GetMapping("/user")
	public String viewUserPage(Model model) {

		int id_empresa = obtenerUsuario().getIdEmpresa();

		List<DishView> listDish = obtenerPlatosUsuario(id_empresa);
		List<Menu> listMenus = obtenerMenusUsuario(id_empresa);

		model.addAttribute("listMenus", listMenus);
		model.addAttribute("listDish", listDish);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addAttribute("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping(value = "/editor/registrar_nueva_empresa/registrar_empresa_exito", method = RequestMethod.POST)
	public String viewRegistrarNuevaEmpresa2Page(@Valid Empresa empresa, BindingResult bindingResult,
			ModelMap modelMap) {

		int a = 0;

//		ModelAndView model = new ModelAndView();

//		if (bindingResult.hasErrors()) {
//			model.addObject("sucessMessage", "Por favor, corrige los errores");
//			model.addObject("bindingResult", bindingResult);
//
//		} else if (companyService.isCompanyAlreadyPresent(empresa)) {
//			model.addObject("sucessMessage", "Empresa ya existe!");
//
//		} else {
//			companyService.saveCompany(empresa);
//			model.addObject("sucessMessage", "Empresa registrada con éxito!");
//		}
//		model.addObject("empresa", empresa);
//
//		model.setViewName("registrar_empresa_exito");

//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}

		try {

			Statement st = Application.con.createStatement();
			ResultSet rs = st.executeQuery("SELECT cif FROM empresas" + " where cif = '" + empresa.getCif() + "';");

			while (rs.next()) {
				String cif = rs.getString(1);

				if (cif != null) {
					return "redirect:/editor";

				}
			}
			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		companyRepo.save(empresa);
		return "redirect:/editor";
	}

	@RequestMapping("/editor/edit/{id_empresa}")
	public ModelAndView editarEmpresa(@PathVariable("id_empresa") int id) {

		ModelAndView model = new ModelAndView("editar_empresa");

		try {

			model.addObject("empresa", companyService.get(id));

		} catch (CompanyNotfound e) {
			e.printStackTrace();
		}
		Integer idComp = obtenerUsuario().getIdEmpresa();
		if (idComp != null) {
			String company = companyRepo.findById(idComp).get().getNombre();
			model.addObject("company", company);
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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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
	public String viewRegistrarNuevoUsuario(User user, BindingResult bindingResult, ModelMap modelMap) {

//		ModelAndView model = new ModelAndView("registrar_usuario_exito");
//
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);

		if (user.getIdEmpresa() == -1) {
			user.setIdEmpresa(null);
		}

		userRepo.save(user);

//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}

		return "redirect:/editor";
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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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
			st.execute("delete from locales where id_local=" + local + ";");

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "redirect:/editor";
	}

	@RequestMapping("/editor/registrar_nuevo_local/exito")
	public String viewRegistrarNuevoLocal(Local local, BindingResult bindingResult, ModelMap modelMap) {

//		ModelAndView model = new ModelAndView("registrar_local_exito");

		localRepo.save(local);
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
////			model.addObject("company", company);
//		}

		return "redirect:/editor";
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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@PostMapping("/admin/crear_nuevo_plato/ingredientes")
	public String escogerIngredientes(DishObj dishObj, RedirectAttributes redirectAttributes) {

//		ModelAndView model = new ModelAndView("ingredientes");

		Dish dish = new Dish();
		dish.setNombre_plato(dishObj.getNombre_plato());
		dish.setDescripcion(dishObj.getDescripcion());
		dish.setId_tipo_platos(dishObj.getTypeDish());
		dish.setId_empresa(obtenerUsuario().getIdEmpresa());

		Dish dish2 = dishRepo.save(dish);

		redirectAttributes.addAttribute("id_plato", dish2.getId_plato());

		return "redirect:/admin/crear_nuevo_plato/ingredientes{id_plato}";
//		model.addObject("id_plato", dish2.getId_plato());
//		List<FoodView> listFood = foodViewRepo.findAll();
//		model.addObject("listFood", listFood);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}

//		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/ingredientes{id_plato}")
	public ModelAndView escogerIngredientes2(@PathVariable("id_plato") int id_plato, FoodAmountObj foodAmountObj) {

		ModelAndView model = new ModelAndView("ingredientes");

		if (foodAmountObj.getId_alimento() != null) {
			try {
				Statement st = Application.con.createStatement();

				st.execute("insert into platos_alimentos (idPlato, idAlimento, cantidad) values( " + id_plato + ", "
						+ foodAmountObj.getId_alimento() + "," + foodAmountObj.getAmount() + ");");
				st.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		model.addObject("id_plato", id_plato);
		List<FoodView> listFood = foodViewRepo.findAll();
		model.addObject("listFood", listFood);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

//	/admin/ver_ingredientes_plato/' + ${id_plato}

	@RequestMapping("/admin/ver_ingredientes_plato/{id_plato}")
	public ModelAndView ver_ingredientes_plato(@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("terminar_plato");

//		listIngredientes
		Map<String, BigDecimal> listIngredientes = new HashMap<String, BigDecimal>();
		ResultSet rs;
		try {
			Statement st = Application.con.createStatement();
			rs = st.executeQuery("select a.nombre, pa.idAlimento , pa.cantidad \r\n"
					+ "from alimentos as a left join platos_alimentos as pa on a.id_alimento = pa.idAlimento \r\n"
					+ "where pa.idPlato = " + id_plato + ";");

			while (rs.next()) {
				String nombre_alimento = rs.getString(1);
				BigDecimal cantidad = rs.getBigDecimal(3);
				listIngredientes.put(nombre_alimento, cantidad);

			}
			model.addObject("listIngredientes", listIngredientes);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/ingredientes/{id_alimento}/{id_plato}")
	public ModelAndView añadirIngrediente(@PathVariable("id_alimento") int id_alimento,
			@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("opciones_ingrediente");

		Food food = foodRepo.findByIdAlimento(id_alimento);
		listFoodDish.add(food);
		dishObj.setMapFood(ingredientes);

		List<Food> listFood = foodRepo.findAll();

		FoodAmountObj foodAmountObj = new FoodAmountObj();
		foodAmountObj.setFood(food.getNombre());

		model.addObject("listFood", listFood);
		model.addObject("foodAmountObj", foodAmountObj);
		model.addObject("id_plato", id_plato);
		model.addObject("id_alimento", id_alimento);
		model.addObject("name_food", foodRepo.findById(id_alimento).get().getNombre());
		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping("/admin/crear_nuevo_plato/terminarPlatoExito")
	public String terminarPlatoExito() {

		Dish dish = new Dish();
		dish.setNombre_plato(dishObj.getNombre_plato());
		dish.setDescripcion(dishObj.getDescripcion());

		dish.setId_empresa(obtenerUsuario().getIdEmpresa());

		dish.setId_tipo_platos(dishObj.getTypeDish());

		dishRepo.save(dish);

		for (Entry<String, BigDecimal> ingrediente : dishObj.getMapFood().entrySet()) {

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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		List<DishIngredients> listDishIngredients = new ArrayList<DishIngredients>();

		ResultSet rs;
		try {
			Statement st = Application.con.createStatement();
			rs = st.executeQuery("select a.nombre, pa.idAlimento , pa.cantidad \r\n"
					+ "from alimentos as a left join platos_alimentos as pa on a.id_alimento = pa.idAlimento \r\n"
					+ "where pa.idPlato = " + id_plato + ";");

			while (rs.next()) {
				String nombre_alimento = rs.getString(1);
				Integer id_alimento = rs.getInt(2);
				BigDecimal cantidad = rs.getBigDecimal(3);

				DishIngredients dishIngredients = new DishIngredients(nombre_alimento, id_alimento, cantidad);
				listDishIngredients.add(dishIngredients);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.addObject("listDishIngredients", listDishIngredients);

		return model;
	}

	@RequestMapping({ "/admin/editMenu/{id_menu}" })
	public ModelAndView editarMenu(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("");
//		ModelAndView model = new ModelAndView("editar_menu");
//
//		Menu menu = menuRepo.findById(id_menu).get();
//
//		model.addObject("menu", menu);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}

		if (menuRepo.findById(id_menu).get().getDescripcion().equals("Menú individual")) {

			model.setViewName("editar_menu");

			Menu menu = menuRepo.findById(id_menu).get();
			MenuObj menuObj = new MenuObj();
			menuObj.setName_menu(menu.getNombre_menu());
			try {
				Statement st1 = Application.con.createStatement();
				ResultSet rs1;

				rs1 = st1.executeQuery("select mp.idPlato, p.nombre_plato, tp.id_tipos_platos \r\n"
						+ "from menus_platos as mp join platos as p on mp.idPlato=p.id_plato\r\n"
						+ "left join tiposplatos as tp on  p.id_tipo_platos= tp.id_tipos_platos \r\n"
						+ "where idMenu = " + id_menu + ";");

				while (rs1.next()) {
					Integer id_plato = rs1.getInt(1);
					String nombre_plato = rs1.getString(2);
					Integer id_tipo_plato = rs1.getInt(3);

					if (id_tipo_plato == 1) {
						menuObj.setFirst_dish(id_plato);
					} else if (id_tipo_plato == 2) {
						menuObj.setSecond_dish(id_plato);
					} else if (id_tipo_plato == 3) {
						menuObj.setThird_dish(id_plato);
					}

				}
				rs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());
			List<LocalEnable> listLocalsEnable = new ArrayList<LocalEnable>();
			boolean enable = true;
			for (Local local : listLocals) {

				try {
					Statement st2 = Application.con.createStatement();
					ResultSet rs2;

//					Ver que locales estan asociados a ese menu
					rs2 = st2.executeQuery("select * from locales_menus where idLocal=" + local.getIdLocal()
							+ " and idMenu=" + id_menu + ";");
					while (rs2.next()) {
						Integer id_local = rs2.getInt(1);

//						Cuando existe local asociado a menu lo guardo como enable
						if (id_local == null) {
							enable = false;
						} else {
							enable = true;
						}
						LocalEnable localEnable = new LocalEnable(local.getNombre(), local.getDireccion(),
								local.getIdLocal(), local.getIdEmpresa(), enable);

						listLocalsEnable.add(localEnable);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			model.addObject("listLocals", listLocalsEnable);
			model.addObject("menuObj", menuObj);
			model.addObject("name_menu", menu.getNombre_menu());

			Integer id = obtenerUsuario().getIdEmpresa();
			if (id != null) {
				String company = companyRepo.findById(id).get().getNombre();
				model.addObject("company", company);
			}

			try {
				Statement st1 = Application.con.createStatement();
				ResultSet rs1;

				rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
						+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
						+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
						+ id_menu + ";");

				List<Dish> listDish2 = new ArrayList<Dish>();
				while (rs1.next()) {
					String tipo_plato = rs1.getString(1);
					Integer id_plato = rs1.getInt(2);
					String nombre_plato = rs1.getString(3);
					Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
					listDish2.add(dish);
				}
				model.addObject("listDish2", listDish2);
				rs1.close();

				Statement st = Application.con.createStatement();
				ResultSet rs = st.executeQuery(
						"select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

				List<Dish> listDish = new ArrayList<Dish>();
				while (rs.next()) {
					Integer id_plato = rs.getInt(1);
					String nombre_plato = rs.getString(3);
					Dish dish = new Dish(id_plato, nombre_plato);
					listDish.add(dish);
				}
				model.addObject("listDish", listDish);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List<Dish> listDish = dishRepo.findAll();
			int id_company = obtenerUsuario().getIdEmpresa();

			List<Dish> listDishCompany1 = new ArrayList<Dish>();
			List<Dish> listDishCompany2 = new ArrayList<Dish>();
			List<Dish> listDishCompany3 = new ArrayList<Dish>();

			for (int i = 0; i < listDish.size(); i++) {
				Integer id_empresa = listDish.get(i).getId_empresa();
				if (id_empresa != null && id_empresa == id_company) {

					if (listDish.get(i).getId_tipo_platos() == 1) {
						listDishCompany1.add(listDish.get(i));
					} else if (listDish.get(i).getId_tipo_platos() == 2) {
						listDishCompany2.add(listDish.get(i));
					} else if (listDish.get(i).getId_tipo_platos() == 3) {
						listDishCompany3.add(listDish.get(i));
					}

				}
			}

			model.addObject("listDishCompany1", listDishCompany1);
			model.addObject("listDishCompany2", listDishCompany2);
			model.addObject("listDishCompany3", listDishCompany3);
		} else {
			model.setViewName("editar_menu_grupal_admin");

			Menu menu = menuRepo.findById(id_menu).get();
			MenuObj menuObj = new MenuObj();
			menuObj.setName_menu(menu.getNombre_menu());
			model.addObject("menuObj", menuObj);
			try {
				Statement st1 = Application.con.createStatement();
				ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
						+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
						+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
						+ id_menu + ";");

				List<Dish> listDish2 = new ArrayList<Dish>();
				while (rs1.next()) {
					String tipo_plato = rs1.getString(1);
					Integer id_plato = rs1.getInt(2);
					String nombre_plato = rs1.getString(3);
					Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
					listDish2.add(dish);
				}
				model.addObject("listDish2", listDish2);
				rs1.close();

				Statement st = Application.con.createStatement();
				ResultSet rs = st.executeQuery(
						"select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

				List<Dish> listDish = new ArrayList<Dish>();
				while (rs.next()) {
					Integer id_plato = rs.getInt(1);
					String nombre_plato = rs.getString(3);
					Dish dish = new Dish(id_plato, nombre_plato);
					listDish.add(dish);
				}
				model.addObject("listDish", listDish);
				rs.close();

				GroupalDish groupalDish = new GroupalDish();
				groupalDish.setName_menu(menu.getNombre_menu());
//				groupalDish.setId_menu(menu.getId_menu());

				int select = 0;
				model.addObject("id_menu", id_menu);
				model.addObject("select", select);
				model.addObject("groupalDish", groupalDish);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return model;
	}

	@RequestMapping({ "/user/editMenu/{id_menu}" })
	public ModelAndView editarMenu_user(@PathVariable("id_menu") int id_menu) {
		ModelAndView model = new ModelAndView();
		if (menuRepo.findById(id_menu).get().getDescripcion().equals("Menú individual")) {

			model.setViewName("editar_menu_user");

			Menu menu = menuRepo.findById(id_menu).get();
			MenuObj menuObj = new MenuObj();
			menuObj.setName_menu(menu.getNombre_menu());
			try {
				Statement st1 = Application.con.createStatement();
				ResultSet rs1;

				rs1 = st1.executeQuery("select mp.idPlato, p.nombre_plato, tp.id_tipos_platos \r\n"
						+ "from menus_platos as mp join platos as p on mp.idPlato=p.id_plato\r\n"
						+ "left join tiposplatos as tp on  p.id_tipo_platos= tp.id_tipos_platos \r\n"
						+ "where idMenu = " + id_menu + ";");

				while (rs1.next()) {
					Integer id_plato = rs1.getInt(1);
					String nombre_plato = rs1.getString(2);
					Integer id_tipo_plato = rs1.getInt(3);

					if (id_tipo_plato == 1) {
						menuObj.setFirst_dish(id_plato);
					} else if (id_tipo_plato == 2) {
						menuObj.setSecond_dish(id_plato);
					} else if (id_tipo_plato == 3) {
						menuObj.setThird_dish(id_plato);
					}

				}
				rs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());
			List<LocalEnable> listLocalsEnable = new ArrayList<LocalEnable>();
			boolean enable = true;
			for (Local local : listLocals) {

				try {
					Statement st2 = Application.con.createStatement();
					ResultSet rs2;

//					Ver que locales estan asociados a ese menu
					rs2 = st2.executeQuery("select * from locales_menus where idLocal=" + local.getIdLocal()
							+ " and idMenu=" + id_menu + ";");
					while (rs2.next()) {
						Integer id_local = rs2.getInt(1);

//						Cuando existe local asociado a menu lo guardo como enable
						if (id_local == null) {
							enable = false;
						} else {
							enable = true;
						}
						LocalEnable localEnable = new LocalEnable(local.getNombre(), local.getDireccion(),
								local.getIdLocal(), local.getIdEmpresa(), enable);

						listLocalsEnable.add(localEnable);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			model.addObject("listLocals", listLocalsEnable);
			model.addObject("menuObj", menuObj);
			model.addObject("name_menu", menu.getNombre_menu());

			Integer id = obtenerUsuario().getIdEmpresa();
			if (id != null) {
				String company = companyRepo.findById(id).get().getNombre();
				model.addObject("company", company);
			}

			try {
				Statement st1 = Application.con.createStatement();
				ResultSet rs1;

				rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
						+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
						+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
						+ id_menu + ";");

				List<Dish> listDish2 = new ArrayList<Dish>();
				while (rs1.next()) {
					String tipo_plato = rs1.getString(1);
					Integer id_plato = rs1.getInt(2);
					String nombre_plato = rs1.getString(3);
					Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
					listDish2.add(dish);
				}
				model.addObject("listDish2", listDish2);
				rs1.close();

				Statement st = Application.con.createStatement();
				ResultSet rs = st.executeQuery(
						"select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

				List<Dish> listDish = new ArrayList<Dish>();
				while (rs.next()) {
					Integer id_plato = rs.getInt(1);
					String nombre_plato = rs.getString(3);
					Dish dish = new Dish(id_plato, nombre_plato);
					listDish.add(dish);
				}
				model.addObject("listDish", listDish);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List<Dish> listDish = dishRepo.findAll();
			int id_company = obtenerUsuario().getIdEmpresa();

			List<Dish> listDishCompany1 = new ArrayList<Dish>();
			List<Dish> listDishCompany2 = new ArrayList<Dish>();
			List<Dish> listDishCompany3 = new ArrayList<Dish>();

			for (int i = 0; i < listDish.size(); i++) {
				Integer id_empresa = listDish.get(i).getId_empresa();
				if (id_empresa != null && id_empresa == id_company) {

					if (listDish.get(i).getId_tipo_platos() == 1) {
						listDishCompany1.add(listDish.get(i));
					} else if (listDish.get(i).getId_tipo_platos() == 2) {
						listDishCompany2.add(listDish.get(i));
					} else if (listDish.get(i).getId_tipo_platos() == 3) {
						listDishCompany3.add(listDish.get(i));
					}

				}
			}

			model.addObject("listDishCompany1", listDishCompany1);
			model.addObject("listDishCompany2", listDishCompany2);
			model.addObject("listDishCompany3", listDishCompany3);
		} else {
			model.setViewName("editar_menu_grupal_user");

			Menu menu = menuRepo.findById(id_menu).get();
			MenuObj menuObj = new MenuObj();
			menuObj.setName_menu(menu.getNombre_menu());
			model.addObject("menuObj", menuObj);
			try {
				Statement st1 = Application.con.createStatement();
				ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
						+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
						+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
						+ id_menu + ";");

				List<Dish> listDish2 = new ArrayList<Dish>();
				while (rs1.next()) {
					String tipo_plato = rs1.getString(1);
					Integer id_plato = rs1.getInt(2);
					String nombre_plato = rs1.getString(3);
					Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
					listDish2.add(dish);
				}
				model.addObject("listDish2", listDish2);
				rs1.close();

				Statement st = Application.con.createStatement();
				ResultSet rs = st.executeQuery(
						"select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

				List<Dish> listDish = new ArrayList<Dish>();
				while (rs.next()) {
					Integer id_plato = rs.getInt(1);
					String nombre_plato = rs.getString(3);
					Dish dish = new Dish(id_plato, nombre_plato);
					listDish.add(dish);
				}
				model.addObject("listDish", listDish);
				rs.close();

				GroupalDish groupalDish = new GroupalDish();
				groupalDish.setName_menu(menu.getNombre_menu());
//				groupalDish.setId_menu(menu.getId_menu());

				int select = 0;
				model.addObject("id_menu", id_menu);
				model.addObject("select", select);
				model.addObject("groupalDish", groupalDish);

			} catch (SQLException e) {
				e.printStackTrace();
			}

//			<div class ="row">
//			<table class="table" style="margin-top:40px;">
//			  <thead>
//			    <tr >
//				  <th scope="col">Tipo de plato</th>
//			      <th scope="col">Nombre de plato</th>
//			      <th scope="col" data-field="action" style="margin-left:10%;margin-right:10%;width:15%;">Operación</th>
//			    </tr>
//			  </thead>
//			  <tbody id="myTable">
//			    <tr th:each="dish: ${listDish2}">
//                    <td th:text="${dish.tipo_plato}"></td>
//                    <td th:text="${dish.nombre_plato}"></td>
//                	<td >
//						 <div class="botonesEdicion" >
//				        	<a th:href="@{'/user/eliminarPlato_menu_grupal/' + ${id_menu} + '/' + ${dish.id_plato}}" class="btn btn-info btn-lg" style="margin-left:20px;background-color:#FF3333;border-color:#FF3333;;color:#ffffff;font-size:0.9rem; " >Eliminar</a>
//						 </div>     	                    	
//                    </td>
//			    </tr>
//			  </tbody>
//			</table>
//		</div>
		}

		return model;
	}

	@PostMapping({ "/admin/saveMenu/{id_menu}" })
	public String guardarMenu(MenuObj menuObj, @PathVariable("id_menu") int id_menu) {

//		Menu menuObj = menuRepo.findById(menu.getId_menu()).get();
//		menuObj.setNombre_menu(menu.getNombre_menu());
//
//		menuRepo.save(menuObj);

		Statement st;
		try {
			List<Integer> listPlatos = new ArrayList<Integer>();

			st = Application.con.createStatement();

//			Obtener platos viejos
			ResultSet rs = st.executeQuery("select idPlato from menus_platos\r\n" + "where idMenu = " + id_menu + ";");

			while (rs.next()) {
				Integer id_plato = rs.getInt(1);
				listPlatos.add(id_plato);
			}
			rs.close();

			String query = "delete from menus_platos where idMenu=" + id_menu + ";";
			st.execute(query);

			query = "insert into menus_platos (idMenu, idPlato) values(" + id_menu + "," + menuObj.getFirst_dish()
					+ ");";
			st.execute(query);

			query = "insert into menus_platos (idMenu, idPlato) values(" + id_menu + "," + menuObj.getSecond_dish()
					+ ");";
			st.execute(query);

			if (menuObj.getThird_dish() != null) {
				query = "insert into menus_platos (idMenu, idPlato) values(" + id_menu + "," + menuObj.getThird_dish()
						+ ");";
				st.execute(query);
			}

//			query = "delete from locales_menus\r\n" + "where idMenu = " + id_menu + ";";
//			st.execute(query);
//
//			for (int i = 0; i < menuObj.getList_id_local().size(); i++) {
//				String query2 = "insert into locales_menus values(" + menuObj.getList_id_local().get(i) + ", " + id_menu
//						+ ");";
//				st.execute(query2);
//			}

			st.close();

			Menu menu = menuRepo.findById(id_menu).get();
			menu.setNombre_menu(menuObj.getName_menu());
			menuRepo.save(menu);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/admin";
	}

	@PostMapping({ "/user/saveMenu/{id_menu}" })
	public String guardarMenu_user(MenuObj menuObj, @PathVariable("id_menu") int id_menu) {

		Statement st;
		try {
			List<Integer> listPlatos = new ArrayList<Integer>();

			st = Application.con.createStatement();

//			Obtener platos viejos
			ResultSet rs = st.executeQuery("select idPlato from menus_platos\r\n" + "where idMenu = " + id_menu + ";");

			while (rs.next()) {
				Integer id_plato = rs.getInt(1);
				listPlatos.add(id_plato);
			}
			rs.close();

			String query = "delete from menus_platos where idMenu=" + id_menu + ";";
			st.execute(query);

			query = "insert into menus_platos (idMenu, idPlato) values(" + id_menu + "," + menuObj.getFirst_dish()
					+ ");";
			st.execute(query);

			query = "insert into menus_platos (idMenu, idPlato) values(" + id_menu + "," + menuObj.getSecond_dish()
					+ ");";
			st.execute(query);

			query = "insert into menus_platos (idMenu, idPlato) values(" + id_menu + "," + menuObj.getThird_dish()
					+ ");";
			st.execute(query);

//			query = "delete from locales_menus\r\n" + "where idMenu = " + id_menu + ";";
//			st.execute(query);
//
//			for (int i = 0; i < menuObj.getList_id_local().size(); i++) {
//				String query2 = "insert into locales_menus values(" + menuObj.getList_id_local().get(i) + ", " + id_menu
//						+ ");";
//				st.execute(query2);
//			}

			st.close();

			Menu menu = menuRepo.findById(id_menu).get();
			menu.setNombre_menu(menuObj.getName_menu());
			menuRepo.save(menu);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/user";
	}

	@PostMapping("/admin/saveDish/{id_plato}")
	public String guardarPlato(Dish dish) {

		dish.setId_empresa(obtenerUsuario().getIdEmpresa());
		dishRepo.save(dish);

		return "redirect:/admin";
	}

//	@RequestMapping("/admin/ComponentesDish/{id_plato}")
//	public ModelAndView mostrarComponentesPlato(@PathVariable("id_plato") int id_plato) {
//
//		ModelAndView model = new ModelAndView("componentes_plato_admin");
//
//		List<ComponentsDishTable> listComponentsDish = obtenerBDcomponentesPlato(id_plato);
//
//		model.addObject("listComponentsDish", listComponentsDish);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}
//
//		return model;
//	}

	public Map<Integer, BigDecimal> obtenerIngredientesPlato(int id_plato) {
		Map<Integer, BigDecimal> mapIngredientAmount = new HashMap<Integer, BigDecimal>();

		try {
			Statement st = Application.con.createStatement();

			ResultSet rs = st.executeQuery(
					"select idAlimento, cantidad from platos_alimentos " + "where idPlato = " + id_plato + ";");

			while (rs.next()) {
				mapIngredientAmount.put(rs.getInt(1), rs.getBigDecimal(2));

			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mapIngredientAmount;
	}

	public Map<String, GroupUnitObj> mapComponentsIngredient;

	public Map<Integer, List<ComponentsFood>> obtenerComponentesPlato(Map<Integer, BigDecimal> mapIngredientAmount) {

		Map<Integer, List<ComponentsFood>> allComponentsDish = new HashMap<Integer, List<ComponentsFood>>();

		// Lista que almacena los componentes de todos los alimentos del plato
		List<ComponentsFood> listaComponentes = new ArrayList<ComponentsFood>();

		// Almacenar el grupo y unidad de los componentess
		mapComponentsIngredient = new HashMap<String, GroupUnitObj>();
		try {
			Statement st2 = Application.con.createStatement();
			for (Entry<Integer, BigDecimal> ingrediente : mapIngredientAmount.entrySet()) {
				ResultSet rs2;

				rs2 = st2.executeQuery(
						"SELECT g.nombre, ac.c_ori_name, ac.best_location, ac.v_unit, ac.mu_descripcion  \r\n"
								+ "FROM nutri_db.alimentos_componentesquimicos as ac left join componentesquimicos as c on ac.c_ori_name = c.c_ori_name \r\n"
								+ "left join gruposcomponentes as g on c.componentgroup_id = g.idGruposComponentes\r\n"
								+ "where idAlimento = " + ingrediente.getKey() + "\r\n" + "and ac.best_location > 0\r\n"
								+ "order by g.idGruposComponentes, v_unit asc;");

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
					mapComponentsIngredient.put(descripcionComponente, groupUnitObj);

				}
				allComponentsDish.put(ingrediente.getKey(), listaComponentes);

				// Reinicio lista de componentes para que los componentes de cada alimento los
				// almacene en una entrada distinta del mapa

				listaComponentes = new ArrayList<ComponentsFood>();

				rs2.close();

			}
			st2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allComponentsDish;

	}

	public Float calcularProporcion(Float amount, String proportion) {
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
		return propor;
	}

	public Map<String, Float> obtenerComponentesUnicos(Map<Integer, BigDecimal> mapIngredientAmount,
			Map<Integer, List<ComponentsFood>> allComponentsDish) {
		Map<String, Float> uniqueComponents = new HashMap<String, Float>();

		for (Entry<Integer, List<ComponentsFood>> ingrediente : allComponentsDish.entrySet()) {

			ingrediente.getKey();
			List<ComponentsFood> listaCompon = ingrediente.getValue();

			for (int i = 0; i < listaCompon.size(); i++) {
				String nameComponent = listaCompon.get(i).getC_ori_name();
				Float amount = listaCompon.get(i).getBest_location();
				String description = listaCompon.get(i).getDescripcion();

				if (uniqueComponents.get(listaCompon.get(i).getC_ori_name()) == null) {

					Float propor = calcularProporcion(amount, description);
					BigDecimal quantity = mapIngredientAmount.get(ingrediente.getKey());

					uniqueComponents.put(nameComponent, propor * quantity.floatValue());

				} else {
					Float value = uniqueComponents.get(nameComponent);
					Float propor = calcularProporcion(amount, description);

					BigDecimal quantity = mapIngredientAmount.get(ingrediente.getKey());
					Float sum = value + propor * quantity.floatValue();
					uniqueComponents.replace(listaCompon.get(i).getC_ori_name(), sum);

				}
			}

		}
		return uniqueComponents;
	}

	public List<ComponentsDishTable> obtenerBDcomponentesPlato(int id_plato) {

		List<ComponentsDishTable> listComponentDishTable = new ArrayList<ComponentsDishTable>();

		Map<Integer, BigDecimal> mapIngredientAmount = obtenerIngredientesPlato(id_plato);

		Map<Integer, List<ComponentsFood>> allComponentsDish = obtenerComponentesPlato(mapIngredientAmount);

		Map<String, Float> uniqueComponents = obtenerComponentesUnicos(mapIngredientAmount, allComponentsDish);

		for (Entry<String, Float> componente : uniqueComponents.entrySet()) {

			ComponentsDishTable componentDishTable = new ComponentsDishTable();

			componentDishTable.setNameComponent(componente.getKey());
			componentDishTable.setAmount(componente.getValue());

			GroupUnitObj groupUnitObj = mapComponentsIngredient.get(componente.getKey());

			componentDishTable.setGroupComponent(groupUnitObj.getGroup());
			componentDishTable.setUnit(groupUnitObj.getUnit());

			listComponentDishTable.add(componentDishTable);

		}

		return listComponentDishTable;
	}

	@RequestMapping("/admin/AlergenosDish/{id_plato}")
	public ModelAndView mostrarAlergenosPlato(@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("alergenos_plato");

		Map<String, String> mapAlergensDish = obtenerAlergenosPlato(id_plato);

		model.addObject("mapAlergensDish", mapAlergensDish);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

	@RequestMapping({ "/admin/crear_menu_individual" })
	public ModelAndView crearMenuIndividual() {

		ModelAndView model = new ModelAndView("crear_menu_individual");

		List<Dish> listDish = dishRepo.findAll();
		int id_company = obtenerUsuario().getIdEmpresa();
		int select = 0;

		List<Dish> listDishCompany1 = new ArrayList<Dish>();
		List<Dish> listDishCompany2 = new ArrayList<Dish>();
		List<Dish> listDishCompany3 = new ArrayList<Dish>();

		for (int i = 0; i < listDish.size(); i++) {
			Integer id_empresa = listDish.get(i).getId_empresa();
			if (id_empresa != null && id_empresa == id_company) {

				if (listDish.get(i).getId_tipo_platos() == 1) {
					listDishCompany1.add(listDish.get(i));
				} else if (listDish.get(i).getId_tipo_platos() == 2) {
					listDishCompany2.add(listDish.get(i));
				} else if (listDish.get(i).getId_tipo_platos() == 3) {
					listDishCompany3.add(listDish.get(i));
				}

			}
		}

		MenuObj menuObj = new MenuObj();
		List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());

		model.addObject("listDishCompany1", listDishCompany1);
		model.addObject("listDishCompany2", listDishCompany2);
		model.addObject("listDishCompany3", listDishCompany3);

		model.addObject("menuObj", menuObj);
		model.addObject("select", select);
		model.addObject("listLocals", listLocals);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping({ "/user/crear_menu_individual" })
	public ModelAndView crearMenuIndividual_user() {

		ModelAndView model = new ModelAndView("crear_menu_individual_user");

		List<Dish> listDish = dishRepo.findAll();
		int id_company = obtenerUsuario().getIdEmpresa();
		int select = 0;

		List<Dish> listDishCompany1 = new ArrayList<Dish>();
		List<Dish> listDishCompany2 = new ArrayList<Dish>();
		List<Dish> listDishCompany3 = new ArrayList<Dish>();

		for (int i = 0; i < listDish.size(); i++) {
			Integer id_empresa = listDish.get(i).getId_empresa();
			if (id_empresa != null && id_empresa == id_company) {

				if (listDish.get(i).getId_tipo_platos() == 1) {
					listDishCompany1.add(listDish.get(i));
				} else if (listDish.get(i).getId_tipo_platos() == 2) {
					listDishCompany2.add(listDish.get(i));
				} else if (listDish.get(i).getId_tipo_platos() == 3) {
					listDishCompany3.add(listDish.get(i));
				}

			}
		}

		MenuObj menuObj = new MenuObj();
		List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());

		model.addObject("listDishCompany1", listDishCompany1);
		model.addObject("listDishCompany2", listDishCompany2);
		model.addObject("listDishCompany3", listDishCompany3);

		model.addObject("menuObj", menuObj);
		model.addObject("select", select);
		model.addObject("listLocals", listLocals);
		model.addObject("selectedLocals", new ArrayList<Local>());
		model.addObject("first_select", "Escoge primer plato");

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

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

			for (int i = 0; i < menuObj.getList_id_local().size(); i++) {
				Statement st2 = Application.con.createStatement();

				String query = "insert into locales_menus values(" + menuObj.getList_id_local().get(i) + ","
						+ menuDb.getId_menu() + ");";
				st2.execute(query);

				st2.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/admin";
	}

	@RequestMapping("/user/crear_menu_individual/guardar")
	public String guardarMenuIndividual_user(MenuObj menuObj) {

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

		try {
			Statement st = Application.con.createStatement();

			for (int i = 0; i < listDishes.size(); i++) {
				if (listDishes.get(i) != null) {
					String query = "insert into menus_platos values(" + menuDb.getId_menu() + "," + listDishes.get(i)
							+ ",'" + menuObj.getName_menu() + "','" + description + "'," + id_company + ");";
					st.execute(query);
				}
			}

			st.close();

			for (int i = 0; i < menuObj.getList_id_local().size(); i++) {
				Statement st2 = Application.con.createStatement();

				String query = "insert into locales_menus values(" + menuObj.getList_id_local().get(i) + ","
						+ menuDb.getId_menu() + ");";
				st2.execute(query);

				st2.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/user";
	}

	@RequestMapping({ "/admin/crear_menu_grupal" })
	public ModelAndView crearMenuGrupal() {

		ModelAndView model = new ModelAndView("crear_menu_grupal");

//		listDishesGroupalMenu = new ArrayList<Integer>();
//
//		MenuLocalObj menuLocal = new MenuLocalObj();
//		List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());
//		int select = 0;
//
//		model.addObject("menuLocal", menuLocal);
//		model.addObject("listLocals", listLocals);
//		model.addObject("select", select);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}

		listDishesGroupalMenu = new ArrayList<Integer>();

		MenuLocalObj menuLocal = new MenuLocalObj();
		List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());
		int select = 0;

		model.addObject("menuLocal", menuLocal);
		model.addObject("listLocals", listLocals);
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	// 1º PASO: menu grupal usuario
	@RequestMapping({ "/user/crear_menu_grupal" })
	public ModelAndView crearMenuGrupal_user() {

		ModelAndView model = new ModelAndView("crear_menu_grupal_user");

		listDishesGroupalMenu = new ArrayList<Integer>();

		MenuLocalObj menuLocal = new MenuLocalObj();
		List<Local> listLocals = localRepo.findByIdEmpresa(obtenerUsuario().getIdEmpresa());
		int select = 0;

		model.addObject("menuLocal", menuLocal);
		model.addObject("listLocals", listLocals);
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	private Menu menu_grupal;

//	@PostMapping("/admin/crear_menu_grupal/guardar")
//	public String crearMenuGrupalGuardar(MenuLocalObj menuLocalObj) {
//
//		Menu menu = new Menu();
//
//		menu.setNombre_menu(menuLocalObj.getNombre_menu());
//		menu.setDescripcion("Menú grupal");
//		menu.setFecha_creacion(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
//		menu.setId_empresa(obtenerUsuario().getIdEmpresa());
//
//		menu_grupal = menuRepo.save(menu);
//
//		try {
//			Statement st2 = Application.con.createStatement();
//
//			String query = "insert into locales_menus values(" + menuLocalObj.getList_id_local().get(0) + ","
//					+ menu_grupal.getId_menu() + ");";
//			st2.execute(query);
//
//			st2.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return "redirect:/admin/crear_menu_grupal/platos";
//	}

	@PostMapping("/user/crear_menu_grupal/guardar")
	public ModelAndView crearMenuGrupalGuardar_user(MenuLocalObj menuLocalObj) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_user");

		Menu menu = new Menu();

		menu.setNombre_menu(menuLocalObj.getNombre_menu());
		menu.setDescripcion("Menú grupal");
		menu.setFecha_creacion(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		menu.setId_empresa(obtenerUsuario().getIdEmpresa());

		menu_grupal = menuRepo.save(menu);

		try {
			for (int i = 0; i < menuLocalObj.getList_id_local().size(); i++) {
				Statement st2 = Application.con.createStatement();

				String query = "insert into locales_menus values(" + menuLocalObj.getList_id_local().get(i) + ","
						+ menu_grupal.getId_menu() + ");";
				st2.execute(query);

				st2.close();
			}
			Statement st = Application.con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

			List<Dish> listDish = new ArrayList<Dish>();
			while (rs.next()) {
				Integer id_plato = rs.getInt(1);
				String nombre_plato = rs.getString(3);
				Dish dish = new Dish(id_plato, nombre_plato);
				listDish.add(dish);
			}
			model.addObject("listDish", listDish);
			rs.close();

			Statement st1 = Application.con.createStatement();
			ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
					+ menu_grupal.getId_menu() + ";");

			List<Dish> listDish2 = new ArrayList<Dish>();
			while (rs1.next()) {
				String tipo_plato = rs.getString(1);
				Integer id_plato = rs.getInt(2);
				String nombre_plato = rs.getString(3);
				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
				listDish2.add(dish);
			}
			model.addObject("listDish2", listDish2);
			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		GroupalDish groupalDish = new GroupalDish();
		int select = 0;
		model.addObject("groupalDish", groupalDish);
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		model.addObject("id_menu", menu_grupal.getId_menu());

		return model;
	}

	@PostMapping("/admin/crear_menu_grupal/guardar")
	public ModelAndView crearMenuGrupalGuardar_admin(MenuLocalObj menuLocalObj) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_admin");

		Menu menu = new Menu();

		menu.setNombre_menu(menuLocalObj.getNombre_menu());
		menu.setDescripcion("Menú grupal");
		menu.setFecha_creacion(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		menu.setId_empresa(obtenerUsuario().getIdEmpresa());

		menu_grupal = menuRepo.save(menu);

		try {
			for (int i = 0; i < menuLocalObj.getList_id_local().size(); i++) {
				Statement st2 = Application.con.createStatement();

				String query = "insert into locales_menus values(" + menuLocalObj.getList_id_local().get(i) + ","
						+ menu_grupal.getId_menu() + ");";
				st2.execute(query);

				st2.close();
			}
			Statement st = Application.con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

			List<Dish> listDish = new ArrayList<Dish>();
			while (rs.next()) {
				Integer id_plato = rs.getInt(1);
				String nombre_plato = rs.getString(3);
				Dish dish = new Dish(id_plato, nombre_plato);
				listDish.add(dish);
			}
			model.addObject("listDish", listDish);
			rs.close();

			Statement st1 = Application.con.createStatement();
			ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
					+ menu_grupal.getId_menu() + ";");

			List<Dish> listDish2 = new ArrayList<Dish>();
			while (rs1.next()) {
				String tipo_plato = rs.getString(1);
				Integer id_plato = rs.getInt(2);
				String nombre_plato = rs.getString(3);
				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
				listDish2.add(dish);
			}
			model.addObject("listDish2", listDish2);
			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		GroupalDish groupalDish = new GroupalDish();
		int select = 0;
		model.addObject("groupalDish", groupalDish);
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		model.addObject("id_menu", menu_grupal.getId_menu());

		return model;
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

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

//	@GetMapping("/user/crear_menu_grupal/platos")
//	public ModelAndView escogerPlatosMenuGrupal_user() {
//		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_user");
//
//		GroupalDish groupalDish = new GroupalDish();
//		int select = 0;
//		List<Dish> listDish = dishRepo.findAll();
//		model.addObject("groupalDish", groupalDish);
//		model.addObject("select", select);
//		model.addObject("listDish", listDish);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}
//		
//		
//		return model;
//	}

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

	@RequestMapping("/user/crear_menu_grupal/guardarPlato_editar/{id_menu}")
	public String añadirPlatoMenuGrupal_userEditar(GroupalDish groupalDish, @PathVariable("id_menu") int id_menu,
			RedirectAttributes redirectAttributes) {

		Menu menu = menuRepo.findById(id_menu).get();

		if (groupalDish.getId_dish() != 0) {
			try {
				Statement st = Application.con.createStatement();

				String query = "insert into menus_platos values(" + id_menu + "," + groupalDish.getId_dish() + ",'"
						+ menu.getNombre_menu() + "','" + menu.getDescripcion() + "'," + menu.getId_empresa() + ");";
				st.execute(query);

				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		redirectAttributes.addAttribute("id_menu", id_menu);

		menu.setNombre_menu(groupalDish.getName_menu());
		menuRepo.save(menu);
		return "redirect:/user/editMenu/{id_menu}";
	}

	@RequestMapping("/admin/crear_menu_grupal/guardarPlato_editar/{id_menu}")
	public String añadirPlatoMenuGrupal_adminEditar(GroupalDish groupalDish, @PathVariable("id_menu") int id_menu,
			RedirectAttributes redirectAttributes) {

		Menu menu = menuRepo.findById(id_menu).get();

		if (groupalDish.getId_dish() != 0) {
			try {
				Statement st = Application.con.createStatement();

				String query = "insert into menus_platos values(" + id_menu + "," + groupalDish.getId_dish() + ",'"
						+ menu.getNombre_menu() + "','" + menu.getDescripcion() + "'," + menu.getId_empresa() + ");";
				st.execute(query);

				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		redirectAttributes.addAttribute("id_menu", id_menu);

		menu.setNombre_menu(groupalDish.getName_menu());
		menuRepo.save(menu);
		return "redirect:/admin/editMenu/{id_menu}";
	}

	@RequestMapping("/user/crear_menu_grupal/guardarPlato/{id_menu}")
	public ModelAndView añadirPlatoMenuGrupal_user(GroupalDish groupalDish, @PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_user");

		Menu menu = menuRepo.findById(id_menu).get();
//		model.addObject("id_menu", id_menu);
		if (groupalDish.getId_dish() != 0) {
			try {
				Statement st = Application.con.createStatement();

				String query = "insert into menus_platos values(" + id_menu + "," + groupalDish.getId_dish() + ",'"
						+ menu.getNombre_menu() + "','" + menu.getDescripcion() + "'," + menu.getId_empresa() + ");";
				st.execute(query);

				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			Statement st = Application.con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

			List<Dish> listDish = new ArrayList<Dish>();
			while (rs.next()) {
				Integer id_plato = rs.getInt(1);
				String nombre_plato = rs.getString(3);
				Dish dish = new Dish(id_plato, nombre_plato);
				listDish.add(dish);
			}
			model.addObject("listDish", listDish);
			rs.close();

			Statement st1 = Application.con.createStatement();
			ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
					+ menu_grupal.getId_menu() + ";");

			List<Dish> listDish2 = new ArrayList<Dish>();
			while (rs1.next()) {
				String tipo_plato = rs1.getString(1);
				Integer id_plato = rs1.getInt(2);
				String nombre_plato = rs1.getString(3);
				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
				listDish2.add(dish);
			}
			model.addObject("listDish2", listDish2);
			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		groupalDish = new GroupalDish();
		int select = 0;
		model.addObject("groupalDish", groupalDish);
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		model.addObject("id_menu", menu_grupal.getId_menu());
		return model;
	}

	@RequestMapping("/admin/crear_menu_grupal/guardarPlato/{id_menu}")
	public ModelAndView añadirPlatoMenuGrupal_admin(GroupalDish groupalDish, @PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_admin");

		Menu menu = menuRepo.findById(id_menu).get();

		if (groupalDish.getId_dish() != 0) {
			try {
				Statement st = Application.con.createStatement();

				String query = "insert into menus_platos values(" + id_menu + "," + groupalDish.getId_dish() + ",'"
						+ menu.getNombre_menu() + "','" + menu.getDescripcion() + "'," + menu.getId_empresa() + ");";
				st.execute(query);

				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			Statement st = Application.con.createStatement();
			ResultSet rs = st
					.executeQuery("select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");

			List<Dish> listDish = new ArrayList<Dish>();
			while (rs.next()) {
				Integer id_plato = rs.getInt(1);
				String nombre_plato = rs.getString(3);
				Dish dish = new Dish(id_plato, nombre_plato);
				listDish.add(dish);
			}
			model.addObject("listDish", listDish);
			rs.close();

			Statement st1 = Application.con.createStatement();
			ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
					+ menu_grupal.getId_menu() + ";");

			List<Dish> listDish2 = new ArrayList<Dish>();
			while (rs1.next()) {
				String tipo_plato = rs1.getString(1);
				Integer id_plato = rs1.getInt(2);
				String nombre_plato = rs1.getString(3);
				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
				listDish2.add(dish);
			}
			model.addObject("listDish2", listDish2);
			rs1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		groupalDish = new GroupalDish();
		int select = 0;
		model.addObject("groupalDish", groupalDish);
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		model.addObject("id_menu", menu_grupal.getId_menu());
		return model;
	}

	@RequestMapping(value = { "/user/eliminarPlato_menu_grupal/{id_menu}/{id_plato}" })
//	/user/eliminarPlato_menu_grupal/' + ${id_menu} + '/' + ${dish.id_plato}
	public String eliminarPlatoMenuGrupal(@PathVariable("id_menu") int id_menu, @PathVariable("id_plato") int id_plato,
			RedirectAttributes redirectAttributes) {

		try {
			Statement st = Application.con.createStatement();

			String query = "delete from menus_platos where idMenu=" + id_menu + " and idPlato=" + id_plato + " ;";
			st.execute(query);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		redirectAttributes.addAttribute("id_menu", id_menu);

		return "redirect:/user/crear_menu_grupal/guardarPlato/{id_menu}";
	}

	@RequestMapping(value = { "/admin/eliminarPlato_menu_grupal/{id_menu}/{id_plato}" })
//	/user/eliminarPlato_menu_grupal/' + ${id_menu} + '/' + ${dish.id_plato}
	public String eliminarPlatoMenuGrupalAdmin(@PathVariable("id_menu") int id_menu,
			@PathVariable("id_plato") int id_plato, RedirectAttributes redirectAttributes) {

		try {
			Statement st = Application.con.createStatement();

			String query = "delete from menus_platos where idMenu=" + id_menu + " and idPlato=" + id_plato + " ;";
			st.execute(query);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		redirectAttributes.addAttribute("id_menu", id_menu);

		return "redirect:/admin/crear_menu_grupal/guardarPlato/{id_menu}";
	}

	@RequestMapping(value = { "/user/eliminarPlato_menu_grupal_edicion/{id_menu}/{id_plato}" })
//	/user/eliminarPlato_menu_grupal/' + ${id_menu} + '/' + ${dish.id_plato}
	public String eliminarPlatoMenuGrupalEdicion(@PathVariable("id_menu") int id_menu,
			@PathVariable("id_plato") int id_plato, RedirectAttributes redirectAttributes) {

		try {
			Statement st = Application.con.createStatement();

			String query = "delete from menus_platos where idMenu=" + id_menu + " and idPlato=" + id_plato + " ;";
			st.execute(query);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		redirectAttributes.addAttribute("id_menu", id_menu);

		return "redirect:/user/editMenu/{id_menu}";
	}

	@RequestMapping(value = { "/admin/eliminarPlato_menu_grupal_edicion/{id_menu}/{id_plato}" })
//	/user/eliminarPlato_menu_grupal/' + ${id_menu} + '/' + ${dish.id_plato}
	public String eliminarPlatoMenuGrupalEdicionAdmin(@PathVariable("id_menu") int id_menu,
			@PathVariable("id_plato") int id_plato, RedirectAttributes redirectAttributes) {

		try {
			Statement st = Application.con.createStatement();

			String query = "delete from menus_platos where idMenu=" + id_menu + " and idPlato=" + id_plato + " ;";
			st.execute(query);

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		redirectAttributes.addAttribute("id_menu", id_menu);

		return "redirect:/admin/editMenu/{id_menu}";
	}

	@RequestMapping("/user/crear_menu_grupal/guardarPlato_edicion/{id_menu}")
	public ModelAndView añadirPlatoMenuGrupal_userEdicion(GroupalDish groupalDish,
			@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("editar_menu_grupal_user");

//		Menu menu = menuRepo.findById(id_menu).get();
//
//		if (groupalDish.getId_dish() != 0) {
//			try {
//				Statement st = Application.con.createStatement();
//
//				String query = "insert into menus_platos values(" + id_menu + "," + groupalDish.getId_dish() + ",'"
//						+ menu.getNombre_menu() + "','" + menu.getDescripcion() + "'," + menu.getId_empresa() + ");";
//				st.execute(query);
//
//				st.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		try {
//			Statement st = Application.con.createStatement();
//			ResultSet rs = st
//					.executeQuery("select * from platos where id_empresa = " + obtenerUsuario().getIdEmpresa() + ";");
//
//			List<Dish> listDish = new ArrayList<Dish>();
//			while (rs.next()) {
//				Integer id_plato = rs.getInt(1);
//				String nombre_plato = rs.getString(3);
//				Dish dish = new Dish(id_plato, nombre_plato);
//				listDish.add(dish);
//			}
//			model.addObject("listDish", listDish);
//			rs.close();
//
//			Statement st1 = Application.con.createStatement();
//			ResultSet rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
//					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
//					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
//					+ menu_grupal.getId_menu() + ";");
//
//			List<Dish> listDish2 = new ArrayList<Dish>();
//			while (rs1.next()) {
//				String tipo_plato = rs1.getString(1);
//				Integer id_plato = rs1.getInt(2);
//				String nombre_plato = rs1.getString(3);
//				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
//				listDish2.add(dish);
//			}
//			model.addObject("listDish2", listDish2);
//			rs1.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		groupalDish = new GroupalDish();
//		int select = 0;
//		model.addObject("groupalDish", groupalDish);
//		model.addObject("select", select);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}
//
//		model.addObject("id_menu", menu_grupal.getId_menu());
		return model;
	}

//	@RequestMapping("/admin/crear_nuevo_plato/terminarMenu")
//	public ModelAndView terminarMenuGrupal() {
//
//		ModelAndView model = new ModelAndView("mostrar_platos_menu");
//		Map<Integer, String> mapDishesMenu = new HashMap<Integer, String>();
//
//		for (int i = 0; i < listDishesGroupalMenu.size(); i++) {
//			int id_dish = listDishesGroupalMenu.get(i);
//			mapDishesMenu.put(id_dish, dishRepo.findById(id_dish).get().getNombre_plato());
//		}
//
//		model.addObject("mapDishesMenu", mapDishesMenu);
//
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}
//
//		return model;
//	}

//	'/user/PlatosMenu/' + ${menu.id_menu}
	@RequestMapping("/user/PlatosMenu/{id_menu}")
	public ModelAndView mostrarPlatosMenu(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("mostrar_platos_menu_user");

		try {
			Statement st1 = Application.con.createStatement();
			ResultSet rs1;
			rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
					+ id_menu + ";");

			List<Dish> listDish2 = new ArrayList<Dish>();
			while (rs1.next()) {
				String tipo_plato = rs1.getString(1);
				Integer id_plato = rs1.getInt(2);
				String nombre_plato = rs1.getString(3);
				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
				listDish2.add(dish);
			}
			model.addObject("listDish2", listDish2);
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		model.addObject("nombreMenu", menuRepo.findById(id_menu).get().getNombre_menu());

		return model;

	}

	@RequestMapping("/admin/PlatosMenu/{id_menu}")
	public ModelAndView mostrarPlatosMenuAdmin(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("mostrar_platos_menu");

		try {
			Statement st1 = Application.con.createStatement();
			ResultSet rs1;
			rs1 = st1.executeQuery("select tp.plato, p.id_plato, p.nombre_plato \r\n"
					+ "from tiposplatos as tp left join platos as p on tp.id_tipos_platos = p.id_tipo_platos\r\n"
					+ "left join menus_platos as mp \r\n" + "on p.id_plato = mp.idPlato \r\n" + "where mp.idMenu="
					+ id_menu + ";");

			List<Dish> listDish2 = new ArrayList<Dish>();
			while (rs1.next()) {
				String tipo_plato = rs1.getString(1);
				Integer id_plato = rs1.getInt(2);
				String nombre_plato = rs1.getString(3);
				Dish dish = new Dish(tipo_plato, id_plato, nombre_plato);
				listDish2.add(dish);
			}
			model.addObject("listDish2", listDish2);
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		model.addObject("nombreMenu", menuRepo.findById(id_menu).get().getNombre_menu());

		return model;

	}

	@RequestMapping("/user/crear_nuevo_plato/terminarMenu")
	public String terminarMenuGrupal_user() {

		return "redirect:/user";
	}

	@RequestMapping("/admin/crear_nuevo_plato/terminarMenu")
	public String terminarMenuGrupal_admin() {

		return "redirect:/admin";
	}

	@RequestMapping("/admin/crear_menu_grupal/terminar")
	public String terminarMenuGrupalExito() {

		return "redirect:/admin";
	}

	@RequestMapping("/user/crear_nuevo_plato/cancelarMenu{id_menu}")
	public String cancelarMenuGrupal(@PathVariable("id_menu") int id_menu) {

		menuRepo.delete(menuRepo.findById(id_menu).get());

		return "redirect:/user";
	}

	@RequestMapping("/admin/crear_nuevo_plato/cancelarMenu{id_menu}")
	public String cancelarMenuGrupalAdmin(@PathVariable("id_menu") int id_menu) {

		menuRepo.delete(menuRepo.findById(id_menu).get());

		return "redirect:/admin";
	}

	@RequestMapping("/user/crear_menu_grupal/terminar")
	public String terminarMenuGrupalExito_user() {

		return "redirect:/user";
	}

	@RequestMapping({ "/admin/deleteMenu/{id_menu}" })
	public String eliminarMenu(@PathVariable("id_menu") int id_menu) {

		menuRepo.delete(menuRepo.findById(id_menu).get());

		return "redirect:/admin";
	}

	@RequestMapping({ "/user/deleteMenu/{id_menu}" })
	public String eliminarMenu_user(@PathVariable("id_menu") int id_menu) {

		menuRepo.delete(menuRepo.findById(id_menu).get());

		return "redirect:/user";
	}

	@GetMapping({ "/admin/AlergenosMenu/{id_menu}" })
	public String obtenerAlergenosMenuIndividual(@PathVariable("id_menu") int id_menu) {

		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();

		if (type_menu.equals("Menú individual")) {
			return "redirect:/admin/alergenos_menu_individual/{id_menu}";

		} else if (type_menu.equals("Menú grupal")) {
			return "redirect:/admin/escoger_platos_menu_grupal_alergenos/{id_menu}";
		}
		return null;

	}

	@GetMapping({ "/user/AlergenosMenu/{id_menu}" })
	public String obtenerAlergenosMenuIndividual_user(@PathVariable("id_menu") int id_menu) {

		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();

		if (type_menu.equals("Menú individual")) {
			return "redirect:/user/alergenos_menu_individual/{id_menu}";

		} else if (type_menu.equals("Menú grupal")) {
			return "redirect:/user/escoger_platos_menu_grupal_alergenos/{id_menu}";
		}
		return null;

	}

	@GetMapping({ "/admin/alergenos_menu_individual/{id_menu}" })
	public ModelAndView alergenosMenusIndividualesAdmin(@PathVariable("id_menu") int id_menu) {
		ModelAndView model = new ModelAndView("alergenos_menu_admin");
		Map<String, String> mapAlergensMenu = obtenerAlergenosMenu(id_menu);
		model.addObject("mapAlergensMenu", mapAlergensMenu);
		model.addObject("id_menu", id_menu);
		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@GetMapping({ "/user/alergenos_menu_individual/{id_menu}" })
	public ModelAndView alergenosMenusIndividuales(@PathVariable("id_menu") int id_menu) {
		ModelAndView model = new ModelAndView("alergenos_menu");
		Map<String, String> mapAlergensMenu = obtenerAlergenosMenu(id_menu);
		model.addObject("mapAlergensMenu", mapAlergensMenu);
		model.addObject("id_menu", id_menu);
		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping({ "/user/alergenos_menu_colectivo/{id_menu}" })
	public ModelAndView alergenosMenusColectivos(MenuObj menuObj, @PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("alergenos_menu");
		model.addObject("id_menu", id_menu);

		Map<String, String> mapAlergensMenu = new HashMap<String, String>();

		if (menuObj.getFirst_dish() != 0) {
			Map<String, String> mapFirstDish = obtenerAlergenosPlato(menuObj.getFirst_dish());

			for (Entry<String, String> alergen : mapFirstDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}
		if (menuObj.getSecond_dish() != 0) {
			Map<String, String> mapSecondDish = obtenerAlergenosPlato(menuObj.getSecond_dish());

			for (Entry<String, String> alergen : mapSecondDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}
		if (menuObj.getThird_dish() != 0) {
			Map<String, String> mapThirdDish = obtenerAlergenosPlato(menuObj.getThird_dish());

			for (Entry<String, String> alergen : mapThirdDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}

		model.addObject("mapAlergensMenu", mapAlergensMenu);

		return model;
	}

	@RequestMapping({ "/admin/alergenos_menu_colectivo/{id_menu}" })
	public ModelAndView alergenosMenusColectivosAdmin(MenuObj menuObj, @PathVariable("id_menu") int id_menu) {
		ModelAndView model = new ModelAndView("alergenos_menu_admin");

		Map<String, String> mapAlergensMenu = new HashMap<String, String>();

		if (menuObj.getFirst_dish() != 0) {
			Map<String, String> mapFirstDish = obtenerAlergenosPlato(menuObj.getFirst_dish());

			for (Entry<String, String> alergen : mapFirstDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}
		if (menuObj.getSecond_dish() != 0) {
			Map<String, String> mapSecondDish = obtenerAlergenosPlato(menuObj.getSecond_dish());

			for (Entry<String, String> alergen : mapSecondDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}
		if (menuObj.getThird_dish() != 0) {
			Map<String, String> mapThirdDish = obtenerAlergenosPlato(menuObj.getThird_dish());

			for (Entry<String, String> alergen : mapThirdDish.entrySet()) {
				mapAlergensMenu.put(alergen.getKey(), alergen.getValue());
			}

		}

		model.addObject("mapAlergensMenu", mapAlergensMenu);

		return model;
	}

//	Permite escoger los platos del menú que se van a consumir para proceder a calcular los alérgeno del mismo
	@GetMapping("/admin/escoger_platos_menu_grupal_alergenos/{id_menu}")
	public ModelAndView escogerPlatosMenuGrupalAlergenos(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_alergenos");

		List<Map<Integer, String>> listMapsTypeDishes = getListMapsTypeDishes(id_menu);

		Map<Integer, String> mapaPlatosTipo1Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo2Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo3Menu = new HashMap<Integer, String>();

		for (int i = 0; i < listMapsTypeDishes.size(); i++) {
			if (i == 0) {
				mapaPlatosTipo1Menu = listMapsTypeDishes.get(0);
			} else if (i == 1) {
				mapaPlatosTipo2Menu = listMapsTypeDishes.get(1);
			} else if (i == 2) {
				mapaPlatosTipo3Menu = listMapsTypeDishes.get(2);
			}
		}

		model.addObject("mapaPlatosTipo1Menu", mapaPlatosTipo1Menu);
		model.addObject("mapaPlatosTipo2Menu", mapaPlatosTipo2Menu);
		model.addObject("mapaPlatosTipo3Menu", mapaPlatosTipo3Menu);
		model.addObject("id_menu", id_menu);
		MenuObj menuObj = new MenuObj();
		model.addObject("menuObj", menuObj);

		int select = 0;
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

//	Permite escoger los platos del menú que se van a consumir para proceder a calcular los alérgeno del mismo
	@GetMapping("/user/escoger_platos_menu_grupal_alergenos/{id_menu}")
	public ModelAndView escogerPlatosMenuGrupalAlergenos_user(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_alergenos_user");

		model.addObject("id_menu", id_menu);
		List<Map<Integer, String>> listMapsTypeDishes = getListMapsTypeDishes(id_menu);

		Map<Integer, String> mapaPlatosTipo1Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo2Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo3Menu = new HashMap<Integer, String>();

		for (int i = 0; i < listMapsTypeDishes.size(); i++) {
			if (i == 0) {
				mapaPlatosTipo1Menu = listMapsTypeDishes.get(0);
			} else if (i == 1) {
				mapaPlatosTipo2Menu = listMapsTypeDishes.get(1);
			} else if (i == 2) {
				mapaPlatosTipo3Menu = listMapsTypeDishes.get(2);
			}
		}

		model.addObject("mapaPlatosTipo1Menu", mapaPlatosTipo1Menu);
		model.addObject("mapaPlatosTipo2Menu", mapaPlatosTipo2Menu);
		model.addObject("mapaPlatosTipo3Menu", mapaPlatosTipo3Menu);

		MenuObj menuObj = new MenuObj();
		model.addObject("menuObj", menuObj);

		int select = 0;
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	public List<Map<Integer, String>> getListMapsTypeDishes(int id_menu) {

		List<Map<Integer, String>> listMapsTypeDishes = new ArrayList<Map<Integer, String>>();

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
					}
					if (i == 2) {
						mapaPlatosTipo2Menu.put(rs.getInt(1), rs.getString(2));
					}
					if (i == 3) {
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

		} catch (SQLException e) {
			e.printStackTrace();
		}
		listMapsTypeDishes.add(mapaPlatosTipo1Menu);
		listMapsTypeDishes.add(mapaPlatosTipo2Menu);
		listMapsTypeDishes.add(mapaPlatosTipo3Menu);

		return listMapsTypeDishes;
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

				for (Entry<String, String> alergenDish : mapAlergensDish.entrySet()) {

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

	@RequestMapping({ "/admin/ComponentesMenu/{id_menu}" })
	public String mostrarComponentesMenu(@PathVariable("id_menu") int id_menu) {

		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();

		if (type_menu.equals("Menú individual")) {
			return "redirect:/admin/componentes_menu_individual/{id_menu}";

		} else if (type_menu.equals("Menú grupal")) {
			return "redirect:/admin/escoger_platos_menu_grupal_componentes/{id_menu}";
		}

		return null;
	}

	@RequestMapping({ "/user/ComponentesMenu/{id_menu}" })
	public String mostrarComponentesMenu_user(@PathVariable("id_menu") int id_menu,
			RedirectAttributes redirectAttributes) {

		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();
		redirectAttributes.addAttribute("id_menu", id_menu);
		if (type_menu.equals("Menú individual")) {

			return "redirect:/user/componentes_menu_individual/{id_menu}";

		} else if (type_menu.equals("Menú grupal")) {
			return "redirect:/user/escoger_platos_menu_grupal_componentes/{id_menu}";
		}

		return null;
	}

//	@RequestMapping({ "/user/ComponentesMenu/{id_menu}" })
//	public String mostrarComponentesMenu7(@PathVariable("id_menu") int id_menu) {
//
//		String type_menu = menuRepo.findById(id_menu).get().getDescripcion();
//
//		if (type_menu.equals("Menú individual")) {
//			return "redirect:/user/componentes_menu_individual/{id_menu}";
//
//		} else if (type_menu.equals("Menú grupal")) {
//			return "redirect:/user/escoger_platos_menu_grupal_componentes/{id_menu}";
//		}
//
//		return null;
//	}

//	Permite escoger los platos del menú que se van a consumir para proceder a calcular los alérgeno del mismo
	@GetMapping("/admin/escoger_platos_menu_grupal_componentes/{id_menu}")
	public ModelAndView escogerPlatosMenuGrupalComponentes(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_componentes");

		List<Map<Integer, String>> listMapsTypeDishes = getListMapsTypeDishes(id_menu);

		Map<Integer, String> mapaPlatosTipo1Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo2Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo3Menu = new HashMap<Integer, String>();

		for (int i = 0; i < listMapsTypeDishes.size(); i++) {
			if (i == 0) {
				mapaPlatosTipo1Menu = listMapsTypeDishes.get(0);
			} else if (i == 1) {
				mapaPlatosTipo2Menu = listMapsTypeDishes.get(1);
			} else if (i == 2) {
				mapaPlatosTipo3Menu = listMapsTypeDishes.get(2);
			}
		}

		model.addObject("mapaPlatosTipo1Menu", mapaPlatosTipo1Menu);
		model.addObject("mapaPlatosTipo2Menu", mapaPlatosTipo2Menu);
		model.addObject("mapaPlatosTipo3Menu", mapaPlatosTipo3Menu);

		MenuObj menuObj = new MenuObj();
		model.addObject("menuObj", menuObj);

		int select = 0;
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

//	Permite escoger los platos del menú que se van a consumir para proceder a calcular los alérgeno del mismo
	@GetMapping("/user/escoger_platos_menu_grupal_componentes/{id_menu}")
	public ModelAndView escogerPlatosMenuGrupalComponentes_user(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("escoger_platos_menu_grupal_componentes_user");

		List<Map<Integer, String>> listMapsTypeDishes = getListMapsTypeDishes(id_menu);

		Map<Integer, String> mapaPlatosTipo1Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo2Menu = new HashMap<Integer, String>();
		Map<Integer, String> mapaPlatosTipo3Menu = new HashMap<Integer, String>();

		for (int i = 0; i < listMapsTypeDishes.size(); i++) {
			if (i == 0) {
				mapaPlatosTipo1Menu = listMapsTypeDishes.get(0);
			} else if (i == 1) {
				mapaPlatosTipo2Menu = listMapsTypeDishes.get(1);
			} else if (i == 2) {
				mapaPlatosTipo3Menu = listMapsTypeDishes.get(2);
			}
		}

		model.addObject("mapaPlatosTipo1Menu", mapaPlatosTipo1Menu);
		model.addObject("mapaPlatosTipo2Menu", mapaPlatosTipo2Menu);
		model.addObject("mapaPlatosTipo3Menu", mapaPlatosTipo3Menu);

		MenuObj menuObj = new MenuObj();
		model.addObject("menuObj", menuObj);

		int select = 0;
		model.addObject("select", select);

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	public Map<String, List<ComponentsDishTable>> clasificarComponentes(List<ComponentsDishTable> listComponentsDish) {

		Map<String, List<ComponentsDishTable>> listOrderedComponents = new HashMap<String, List<ComponentsDishTable>>();

		List<ComponentsDishTable> componentsDishTableProximal = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> componentsDishTableHcarbono = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> componentsDishTableGrasa = new ArrayList<ComponentsDishTable>();

		List<ComponentsDishTable> componentsDishTableVitaminas = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> componentsDishTableMinerales = new ArrayList<ComponentsDishTable>();

		for (int i = 0; i < listComponentsDish.size(); i++) {
			ComponentsDishTable componentsDishTable = listComponentsDish.get(i);

			if (componentsDishTable.getGroupComponent().equals("Proximales")) {
				componentsDishTableProximal.add(componentsDishTable);

			} else if (componentsDishTable.getGroupComponent().equals("Hidratos de Carbono")) {
				componentsDishTableHcarbono.add(componentsDishTable);

			} else if (componentsDishTable.getGroupComponent().equals("Grasas")) {
				componentsDishTableGrasa.add(componentsDishTable);

			} else if (componentsDishTable.getGroupComponent().equals("Vitaminas")) {
				componentsDishTableVitaminas.add(componentsDishTable);

			} else if (componentsDishTable.getGroupComponent().equals("Minerales")) {
				componentsDishTableMinerales.add(componentsDishTable);

			}
		}
		listOrderedComponents.put("minerales", componentsDishTableMinerales);
		listOrderedComponents.put("vitaminas", componentsDishTableVitaminas);
		listOrderedComponents.put("grasas", componentsDishTableGrasa);
		listOrderedComponents.put("hc", componentsDishTableHcarbono);
		listOrderedComponents.put("proximales", componentsDishTableProximal);

		return listOrderedComponents;
	}

	public List<ComponentsDishTable> ordenarPorUnidad(Map<String, List<ComponentsDishTable>> mapComponents) {
		List<ComponentsDishTable> listComponentsDishOrdered = new ArrayList<ComponentsDishTable>();

		listComponentsDishOrdered.addAll(ordenarComponentesPorUnidadCantidad(mapComponents.get("proximales")));
		listComponentsDishOrdered.addAll(ordenarComponentesPorUnidadCantidad(mapComponents.get("hc")));
		listComponentsDishOrdered.addAll(ordenarComponentesPorUnidadCantidad(mapComponents.get("grasas")));
		listComponentsDishOrdered.addAll(ordenarComponentesPorUnidadCantidad(mapComponents.get("vitaminas")));
		listComponentsDishOrdered.addAll(ordenarComponentesPorUnidadCantidad(mapComponents.get("minerales")));

		return listComponentsDishOrdered;
	}

	public Map<String, Float> calculoProximales(Map<String, List<ComponentsDishTable>> mapComponents) {

		Map<String, Float> valoresProximales = new HashMap<String, Float>();

		Float valorProteína = (float) 0;
		float valorEnergético = 0;
		float valorGrasa = 0;

		for (ComponentsDishTable componentsDishTable : mapComponents.get("proximales")) {

			if (componentsDishTable.getNameComponent().equals("proteina, total")) {
				valorProteína += componentsDishTable.getAmount();

			} else if (componentsDishTable.getNameComponent().equals("energía, total")) {
				valorEnergético += componentsDishTable.getAmount();

			} else if (componentsDishTable.getNameComponent().equals("grasa, total (lipidos totales)")) {
				valorGrasa += componentsDishTable.getAmount();
			}
		}
		valoresProximales.put("proteina", valorProteína);
		valoresProximales.put("energia", valorEnergético);
		valoresProximales.put("grasa", valorGrasa);

		return valoresProximales;
	}

	public Map<String, Float> calculoHC(Map<String, List<ComponentsDishTable>> mapComponents) {
		Map<String, Float> valoresHC = new HashMap<String, Float>();

		Float valorOtrosHC = (float) 0;
		Float valorFibra = (float) 0;
		Float valorAzúcar = (float) 0;

		for (ComponentsDishTable componentsDishTable : mapComponents.get("hc")) {

			if (componentsDishTable.getNameComponent().equals("fibra, dietetica total")) {
				valorFibra += componentsDishTable.getAmount();

			} else if (componentsDishTable.getNameComponent().equals("azucares, totales")) {
				valorAzúcar += componentsDishTable.getAmount();
			} else {
				valorOtrosHC += componentsDishTable.getAmount();
			}
		}

		valoresHC.put("fibra", valorFibra);
		valoresHC.put("azucar", valorAzúcar);
		valoresHC.put("otrosHC", valorOtrosHC);

		return valoresHC;
	}

	public Float calcularAcGrasos(Map<String, List<ComponentsDishTable>> mapComponents) {
		Float acGrasos = (float) 0;
		for (ComponentsDishTable componentsDishTable : mapComponents.get("grasas")) {

			if (componentsDishTable.getNameComponent().contains("ácidos grasos")) {
				acGrasos += componentsDishTable.getAmount();

			}
		}
		return acGrasos;
	}

	@RequestMapping("/admin/ComponentesDish/{id_plato}")
	public ModelAndView componentesPlato(@PathVariable("id_plato") int id_plato) {

		ModelAndView model = new ModelAndView("componentes_plato_admin");

		List<ComponentsDishTable> listComponentsDish = obtenerBDcomponentesPlato(id_plato);

		Map<String, List<ComponentsDishTable>> mapComponents = clasificarComponentes(listComponentsDish);

		Map<String, Float> valoresProximales = calculoProximales(mapComponents);
		Map<String, Float> valoresHC = calculoHC(mapComponents);

		Float hcTotales = valoresHC.get("fibra") + valoresHC.get("azucar") + valoresHC.get("otrosHC");
		Float acGrasos = calcularAcGrasos(mapComponents);

		float proporcionGrasas = valoresProximales.get("grasa") * 9;
		float proporcionProteinas = valoresProximales.get("proteina") * 4;
		float proporcionHC = hcTotales * 4;
		float proporcionValorOtrosHC = valoresHC.get("otrosHC") * 4;

		float proporcionValorFibra = valoresHC.get("fibra") * 4;
		float proporcionValorAzúcar = valoresHC.get("azucar") * 4;
		float proporcionAcGrasos = acGrasos * 9;

		float total = valoresProximales.get("energia") + proporcionGrasas + proporcionProteinas + proporcionHC;

		List<ComponentsDishTable> listComponentsDishOrdered = ordenarPorUnidad(mapComponents);

//		Cargo datos de tabla completa
		model.addObject("listComponentsDish", listComponentsDishOrdered);

//		Cargo datos de tablas 
		model.addObject("componentsDishTableProximal", mapComponents.get("proximales"));
		model.addObject("componentsDishTableHcarbono", mapComponents.get("hc"));
		model.addObject("componentsDishTableGrasa", mapComponents.get("grasas"));
		model.addObject("componentsDishTableVitaminas", mapComponents.get("vitaminas"));
		model.addObject("componentsDishTableMinerales", mapComponents.get("minerales"));

//		Cargo datos del grafico
		model.addObject("porcentajeGrasa", calcularPorcentaje(proporcionGrasas, total));
		model.addObject("porcentajeProteinas", calcularPorcentaje(proporcionProteinas, total));
		model.addObject("porcentajeHC", calcularPorcentaje(proporcionHC, total));
		model.addObject("porcentajeEnergia", calcularPorcentaje(valoresProximales.get("energia"), total));
		model.addObject("porcentajeOtrosHC", calcularPorcentaje(proporcionValorOtrosHC, total));
		model.addObject("porcentajeFibra", calcularPorcentaje(proporcionValorFibra, total));
		model.addObject("porcentajeAzucar", calcularPorcentaje(proporcionValorAzúcar, total));
		model.addObject("porcentajeAcGrasos", calcularPorcentaje(proporcionAcGrasos, total));

		model.addObject("porcentajeOtrasGrasas",
				round(calcularPorcentaje(proporcionGrasas, total) - calcularPorcentaje(proporcionAcGrasos, total), 2));

		mostrarEmpresa(model);

		return model;
	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public double calcularPorcentaje(float proporcionGrasas, float total) {
		float porcentajeGrasa2 = proporcionGrasas / total;
		return Math.round(porcentajeGrasa2 * 100 * 100) / 100.0;
	}

	public void mostrarEmpresa(ModelAndView model) {
		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}
	}

	public List<ComponentsDishTable> obtenerComponentesMenuGrupal(List<Integer> listDishes){
		List<ComponentsDishTable> listComponentsDishAux = new ArrayList<ComponentsDishTable>();
		
		for (int i = 0; i < listDishes.size(); i++) {
			if (listDishes.get(i) != 0) {

				List<ComponentsDishTable> listComponentsDish = obtenerBDcomponentesPlato(listDishes.get(i));
				for (int j = 0; j < listComponentsDish.size(); j++) {

					boolean flag = false;
					for (int k = 0; k < listComponentsDishAux.size(); k++) {
						if (listComponentsDishAux.get(k).getNameComponent()
								.equals(listComponentsDish.get(j).getNameComponent())) {
							flag = true;
							listComponentsDishAux.get(k).setAmount(
									listComponentsDishAux.get(k).getAmount() + listComponentsDish.get(j).getAmount());
						}
					}
					if (!flag) {
						listComponentsDishAux.add(listComponentsDish.get(j));

					}
					flag = false;

				}
			}

		}
		return listComponentsDishAux;
	}
	
	public void obtenerComponentesMenucolectivo(MenuObj menuObj, ModelAndView model) {
		List<Integer> listDishes = new ArrayList<Integer>();

		if (menuObj.getFirst_dish() != 0) {
			listDishes.add(menuObj.getFirst_dish());

		}
		if (menuObj.getSecond_dish() != 0) {
			listDishes.add(menuObj.getSecond_dish());

		}
		if (menuObj.getThird_dish() != 0) {
			listDishes.add(menuObj.getThird_dish());
		}

		Map<String, List<ComponentsDishTable>> mapComponents = clasificarComponentes(obtenerComponentesMenuGrupal(listDishes));
		Map<String, Float> valoresProximales = calculoProximales(mapComponents);
		Map<String, Float> valoresHC = calculoHC(mapComponents);

		Float hcTotales = valoresHC.get("fibra") + valoresHC.get("azucar") + valoresHC.get("otrosHC");
		Float acGrasos = calcularAcGrasos(mapComponents);

		float proporcionGrasas = valoresProximales.get("grasa") * 9;
		float proporcionProteinas = valoresProximales.get("proteina") * 4;
		float proporcionHC = hcTotales * 4;
		float proporcionValorOtrosHC = valoresHC.get("otrosHC") * 4;

		float proporcionValorFibra = valoresHC.get("fibra") * 4;
		float proporcionValorAzúcar = valoresHC.get("azucar") * 4;
		float proporcionAcGrasos = acGrasos * 9;

		float total = valoresProximales.get("energia") + proporcionGrasas + proporcionProteinas + proporcionHC;

		List<ComponentsDishTable> listComponentsDishOrdered = ordenarPorUnidad(mapComponents);

//		Cargo datos de tabla completa
		model.addObject("listComponentsDish", listComponentsDishOrdered);

//		Cargo datos de tablas 
		model.addObject("componentsDishTableProximal", mapComponents.get("proximales"));
		model.addObject("componentsDishTableHcarbono", mapComponents.get("hc"));
		model.addObject("componentsDishTableGrasa", mapComponents.get("grasas"));
		model.addObject("componentsDishTableVitaminas", mapComponents.get("vitaminas"));
		model.addObject("componentsDishTableMinerales", mapComponents.get("minerales"));

//	Cargo datos del grafico
		model.addObject("porcentajeGrasa", calcularPorcentaje(proporcionGrasas, total));
		model.addObject("porcentajeProteinas", calcularPorcentaje(proporcionProteinas, total));
		model.addObject("porcentajeHC", calcularPorcentaje(proporcionHC, total));
		model.addObject("porcentajeEnergia", calcularPorcentaje(valoresProximales.get("energia"), total));
		model.addObject("porcentajeOtrosHC", calcularPorcentaje(proporcionValorOtrosHC, total));
		model.addObject("porcentajeFibra", calcularPorcentaje(proporcionValorFibra, total));
		model.addObject("porcentajeAzucar", calcularPorcentaje(proporcionValorAzúcar, total));
		model.addObject("porcentajeAcGrasos", calcularPorcentaje(proporcionAcGrasos, total));

		model.addObject("porcentajeOtrasGrasas",
				round(calcularPorcentaje(proporcionGrasas, total) - calcularPorcentaje(proporcionAcGrasos, total), 2));

		mostrarEmpresa(model);

	}
	
	
	@RequestMapping("/admin/componentes_menu_colectivo")
	public ModelAndView componentesMenuColectivo_admin(MenuObj menuObj) {

		ModelAndView model = new ModelAndView("componentes_plato_admin");
		obtenerComponentesMenucolectivo(menuObj, model);

		return model;
	}

	@RequestMapping("/user/componentes_menu_colectivo")
	public ModelAndView componentesMenuColectivo_user(MenuObj menuObj) {

		ModelAndView model = new ModelAndView("componentes_plato");

		obtenerComponentesMenucolectivo(menuObj, model);
		return model;
	}

	@GetMapping(value = { "/user/componentes_menu_individual/{id_menu}" })
	public ModelAndView componentesMenuIndividual(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("componentes_plato");
		obtenerComponentesMenuIndividual(id_menu, model);

		return model;
	}
	
	
	public List<Integer> obtenerPlatosMenu(int id_menu){
		List<Integer> listDishes = new ArrayList<Integer>();
		ResultSet rs;
		try {
			Statement st = Application.con.createStatement();
			rs = st.executeQuery("select idPlato from menus_platos where idMenu = " + id_menu + ";");

			while (rs.next()) {
				listDishes.add(rs.getInt(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listDishes;
	}
	
	public void obtenerComponentesMenuIndividual(int id_menu, ModelAndView model) {
		List<Integer> listDishes = obtenerPlatosMenu(id_menu);

		Map<String, List<ComponentsDishTable>> mapComponents = clasificarComponentes(obtenerComponentesMenuGrupal(listDishes));
		Map<String, Float> valoresProximales = calculoProximales(mapComponents);
		Map<String, Float> valoresHC = calculoHC(mapComponents);

		Float hcTotales = valoresHC.get("fibra") + valoresHC.get("azucar") + valoresHC.get("otrosHC");
		Float acGrasos = calcularAcGrasos(mapComponents);

		float proporcionGrasas = valoresProximales.get("grasa") * 9;
		float proporcionProteinas = valoresProximales.get("proteina") * 4;
		float proporcionHC = hcTotales * 4;
		float proporcionValorOtrosHC = valoresHC.get("otrosHC") * 4;

		float proporcionValorFibra = valoresHC.get("fibra") * 4;
		float proporcionValorAzúcar = valoresHC.get("azucar") * 4;
		float proporcionAcGrasos = acGrasos * 9;

		float total = valoresProximales.get("energia") + proporcionGrasas + proporcionProteinas + proporcionHC;

		List<ComponentsDishTable> listComponentsDishOrdered = ordenarPorUnidad(mapComponents);

//		Cargo datos de tabla completa
		model.addObject("listComponentsDish", listComponentsDishOrdered);

//		Cargo datos de tablas 
		model.addObject("componentsDishTableProximal", mapComponents.get("proximales"));
		model.addObject("componentsDishTableHcarbono", mapComponents.get("hc"));
		model.addObject("componentsDishTableGrasa", mapComponents.get("grasas"));
		model.addObject("componentsDishTableVitaminas", mapComponents.get("vitaminas"));
		model.addObject("componentsDishTableMinerales", mapComponents.get("minerales"));

//	Cargo datos del grafico
		model.addObject("porcentajeGrasa", calcularPorcentaje(proporcionGrasas, total));
		model.addObject("porcentajeProteinas", calcularPorcentaje(proporcionProteinas, total));
		model.addObject("porcentajeHC", calcularPorcentaje(proporcionHC, total));
		model.addObject("porcentajeEnergia", calcularPorcentaje(valoresProximales.get("energia"), total));
		model.addObject("porcentajeOtrosHC", calcularPorcentaje(proporcionValorOtrosHC, total));
		model.addObject("porcentajeFibra", calcularPorcentaje(proporcionValorFibra, total));
		model.addObject("porcentajeAzucar", calcularPorcentaje(proporcionValorAzúcar, total));
		model.addObject("porcentajeAcGrasos", calcularPorcentaje(proporcionAcGrasos, total));

		model.addObject("porcentajeOtrasGrasas",
				round(calcularPorcentaje(proporcionGrasas, total) - calcularPorcentaje(proporcionAcGrasos, total), 2));

		mostrarEmpresa(model);

	}

	@GetMapping({ "/admin/componentes_menu_individual/{id_menu}" })
	public ModelAndView componentesMenuIndividualAdmin(@PathVariable("id_menu") int id_menu) {

		ModelAndView model = new ModelAndView("componentes_plato_admin");

		obtenerComponentesMenuIndividual(id_menu, model);
		return model;
	}


	public List<ComponentsDishTable> ordenarComponentesPorUnidadCantidad(
			List<ComponentsDishTable> componentsDishTableVitaminas) {

		List<ComponentsDishTable> listComponentsDishTable = new ArrayList<ComponentsDishTable>();

		List<ComponentsDishTable> componentsDishTableKcal = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> componentsDishTableG = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> componentsDishTableMG = new ArrayList<ComponentsDishTable>();
		List<ComponentsDishTable> componentsDishTableUG = new ArrayList<ComponentsDishTable>();

		for (ComponentsDishTable componentsDishTable : componentsDishTableVitaminas) {
			if (componentsDishTable.getUnit().equals("kcal") || componentsDishTable.getUnit().equals("kj(kcal)")) {
				componentsDishTableKcal.add(componentsDishTable);

			} else if (componentsDishTable.getUnit().equals("g")) {
				componentsDishTableG.add(componentsDishTable);

			} else if (componentsDishTable.getUnit().equals("mg")) {
				componentsDishTableMG.add(componentsDishTable);

			} else if (componentsDishTable.getUnit().equals("ug")) {
				componentsDishTableUG.add(componentsDishTable);

			}
		}
		listComponentsDishTable.addAll(componentsDishTableKcal);
		listComponentsDishTable.addAll(componentsDishTableG);
		listComponentsDishTable.addAll(componentsDishTableMG);
		listComponentsDishTable.addAll(componentsDishTableUG);

		return listComponentsDishTable;

	}

	@GetMapping({ "/admin/deleteIngredientDish/{id_plato}/{id_ingrediente}" })
	public String eliminarIngredientePlato(@PathVariable("id_plato") Integer id_plato,
			@PathVariable("id_ingrediente") Integer id_ingrediente) {

		try {
			Statement st = Application.con.createStatement();

			st.execute("delete from platos_alimentos where idPlato=" + id_plato.toString() + " and idAlimento = "
					+ id_ingrediente.toString() + ";");
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/admin/editDish/{id_plato}";
	}

	@GetMapping({ "/admin/editIngredientDish/{id_plato}/{id_ingrediente}/{cantidad}" })
	public ModelAndView editarIngredientePlato(@PathVariable("id_plato") Integer id_plato,
			@PathVariable("id_ingrediente") Integer id_ingrediente, @PathVariable("cantidad") BigDecimal cantidad) {

		ModelAndView model = new ModelAndView("editar_ingrediente_plato");

		DishIngredient dishIngredient = new DishIngredient(id_plato, id_ingrediente,
				foodRepo.findByIdAlimento(id_ingrediente).getNombre(), cantidad);

		model.addObject("dishIngredient", dishIngredient);

		return model;
	}

	@RequestMapping(value = {
			"/admin/saveEditIngredientDish/{id_plato}/{id_ingrediente}" }, method = RequestMethod.POST)
	public String editarCantidadIngredientePlato(@PathVariable("id_plato") Integer id_plato,
			@PathVariable("id_ingrediente") Integer id_ingrediente, DishIngredient dishIngredient) {

		try {
			Statement st = Application.con.createStatement();

			st.execute("update platos_alimentos set cantidad = " + dishIngredient.getCantidad() + " where idPlato =  "
					+ id_plato + " and idAlimento = " + id_ingrediente + ";");
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "redirect:/admin/editDish/{id_plato}";
	}

	@RequestMapping(value = { "/admin/addDish/{id_plato}" })
	public ModelAndView añadirIngredientePlato(@PathVariable("id_plato") Integer id_plato) {

		ModelAndView model = new ModelAndView("añadir_ingrediente_plato");

		List<FoodView> listFood = foodViewRepo.findAll();

		model.addObject("id_plato", id_plato);
		model.addObject("listFood", listFood);

		return model;
	}

	@RequestMapping(value = { "/admin/addIngredient/{id_plato}/{id_alimento}" })
	public ModelAndView añadirCantidadIngredientePlato(@PathVariable("id_plato") Integer id_plato,
			@PathVariable("id_alimento") Integer id_ingredient) {

		ModelAndView model = new ModelAndView("opciones_ingrediente_plato");

		List<FoodView> listFood = foodViewRepo.findAll();

		model.addObject("id_plato", id_plato);
		model.addObject("listFood", listFood);

		Food food = foodRepo.findByIdAlimento(id_ingredient);

		FoodAmountObj foodAmountObj = new FoodAmountObj();
		foodAmountObj.setFood(food.getNombre());

		model.addObject("foodAmountObj", foodAmountObj);
//		model.addObject("id_plato", id_plato);
		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping(value = { "/admin/addIngredient/{id_plato}" })
	public String guardarIngredientePlato(@PathVariable("id_plato") Integer id_plato, FoodAmountObj foodAmountObj,
			RedirectAttributes redirectAttributes) {

		try {
			Statement st = Application.con.createStatement();

			st.execute("insert into platos_alimentos (idPlato, idAlimento, cantidad) values( " + id_plato + ", "
					+ foodRepo.findByNameAlimento(foodAmountObj.getFood()).getIdAlimento() + ","
					+ foodAmountObj.getAmount() + ");");
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
		}
		redirectAttributes.addAttribute("id_plato", id_plato);
		return "redirect:/admin/editDish/{id_plato}";
	}

	@RequestMapping(value = { "/admin/IngredientesDish/{id_plato}" })
	public ModelAndView verIngredientePlato(@PathVariable("id_plato") Integer id_plato) {

		ModelAndView model = new ModelAndView("ver_ingredientes_plato");

		List<DishIngredients> listDishIngredients = new ArrayList<DishIngredients>();
		ResultSet rs;
		try {
			Statement st = Application.con.createStatement();
			rs = st.executeQuery("select a.nombre, pa.idAlimento , pa.cantidad \r\n"
					+ "from alimentos as a left join platos_alimentos as pa on a.id_alimento = pa.idAlimento \r\n"
					+ "where pa.idPlato = " + id_plato + ";");

			while (rs.next()) {
				String nombre_alimento = rs.getString(1);
				Integer id_alimento = rs.getInt(2);
				BigDecimal cantidad = rs.getBigDecimal(3);

				DishIngredients dishIngredients = new DishIngredients(nombre_alimento, id_alimento, cantidad);
				listDishIngredients.add(dishIngredients);

			}

			model.addObject("listDishIngredients", listDishIngredients);
			model.addObject("nameDish", dishRepo.findById(id_plato).get().getNombre_plato());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Integer id = obtenerUsuario().getIdEmpresa();
		if (id != null) {
			String company = companyRepo.findById(id).get().getNombre();
			model.addObject("company", company);
		}

		return model;
	}

	@RequestMapping(value = { "/admin/seguir_anadiendo_ingredientes/{id_plato}" })
	public String seguir_anadiendo_ingredientes(@PathVariable("id_plato") Integer id_plato,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addAttribute("id_plato", id_plato);
		return "redirect:/admin/crear_nuevo_plato/ingredientes{id_plato}";
	}

	@RequestMapping(value = { "/admin/terminarPlato" })
	public String terminarPlato2() {

		return "redirect:/admin";
	}


	@RequestMapping(value = { "/admin/cancelar_nuevo_plato/{id_plato}" })
	public String cancelarPlato(@PathVariable("id_plato") Integer id_plato) {
//		ModelAndView model = new ModelAndView("admin");
		try {

			Statement st = Application.con.createStatement();
			st.execute("delete from platos where id_plato=" + id_plato + ";");

			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
//		Integer id = obtenerUsuario().getIdEmpresa();
//		if (id != null) {
//			String company = companyRepo.findById(id).get().getNombre();
//			model.addObject("company", company);
//		}

		return "redirect:/admin";
	}
}
