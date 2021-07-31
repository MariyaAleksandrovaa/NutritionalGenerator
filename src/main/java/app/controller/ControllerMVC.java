package app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControllerMVC {
	

	@RequestMapping(value = {"/prueba", "/"}, method = RequestMethod.GET)
	public ModelAndView viewHomePage2() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("prueba");
		return model;
		
	}	
	
	@RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
	public ModelAndView viewAdminPage() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("admin");
		return model;

	}	
	
	@GetMapping("/editor")
//	@RequestMapping(value = {"/editor"}, method = RequestMethod.GET)
	public String viewEditorPage() {
		
//		ModelAndView model = new ModelAndView();
//		model.setViewName("editor");
//		return model;
		
		return "editor";
	}	
	
	@RequestMapping(value = {"/user"}, method = RequestMethod.GET)
	public ModelAndView viewUserPage() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("user");
		return model;
	}	


}