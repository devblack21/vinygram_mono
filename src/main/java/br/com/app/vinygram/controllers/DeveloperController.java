package br.com.app.vinygram.controllers;

import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import br.com.app.vinygram.service.UserService;

@Controller
@RequestMapping("/dev")
public class DeveloperController {

	@Autowired
	
	private UserService userService;
	
	@RequestMapping("")
	public ModelAndView pageDev(ModelMap modelMap) {
		modelMap.addAttribute("user",userService.getUserAuthenticated());
		modelMap.addAttribute("conteudo","developer/dev");
		modelMap.addAttribute("nick","DevBlack");
		modelMap.addAttribute("bio", "Essa é uma aplicação WEB monolitica \r\n" + 
				"semelhante ao Instagram, na linguagem Java com Spring Framework, com upload de \r\n" + 
				"imagens na nuvem orientado a apectos, persistência de dados no banco MySQL, Thymeleaf,\r\n" + 
				"paginação infinita, entre outras tecnologias...\n\n Aplicação em Desenvolvimento");
		modelMap.addAttribute("nome","Marcos Vinicius André Rocha");
		modelMap.addAttribute("tecns",getTecnologias());
		modelMap.addAttribute("imgURL","https://res.cloudinary.com/devblack/image/upload/v1/developer/dev.jpg");
		
		return new ModelAndView("layouts/autenticado/dev-layout",modelMap);
		
	}
	
	private Set<String> getTecnologias(){
		
		Set<String> conjunto = new TreeSet<String>();
		conjunto.add("Java");
		conjunto.add("Spring Boot");
		conjunto.add("Spring JPA");
		conjunto.add("Spring Security");
		conjunto.add("Thymeleaf");
		conjunto.add("Aspect");
		conjunto.add("JQuery");
		conjunto.add("Bootstrap");
		conjunto.add("Rest");
		conjunto.add("MySQL");
		
		return conjunto;
	}
	
	
	@RequestMapping("/cod")
	public String repositoryGit(ModelMap modelMap) {
	
		return "redirect:https://github.com/devblack21/vinygram_mono";
		
	}
	
	@RequestMapping("/instagram")
	public String intagram(ModelMap modelMap) {
		modelMap.addAttribute("user",userService.getUserAuthenticated());
		modelMap.addAttribute("conteudo","developer/dev");
		
		return "redirect:https://www.instagram.com/dev_viny/?hl=pt-br";
		
	}
	
	@RequestMapping("/github")
	public String github(ModelMap modelMap) {
		modelMap.addAttribute("user",userService.getUserAuthenticated());
		modelMap.addAttribute("conteudo","developer/dev");
		
		return "redirect:https://github.com/devblack21";
		
	}
	
	@RequestMapping("/youtube")
	public String youtube(ModelMap modelMap) {
		modelMap.addAttribute("user",userService.getUserAuthenticated());
		modelMap.addAttribute("conteudo","developer/dev");
		
		return "redirect:https://www.youtube.com/channel/UCoYekXTQx5WBY7qydhHY0UA?view_as=subscriber";
		
	}
	
	@RequestMapping("/linkedin")
	public String linkedin(ModelMap modelMap) {
		modelMap.addAttribute("user",userService.getUserAuthenticated());
		modelMap.addAttribute("conteudo","developer/dev");
		
		return "redirect:https://www.linkedin.com/in/marcos-vinicius-andr%C3%A9-rocha-0aa147146/";
		
	}
	
}