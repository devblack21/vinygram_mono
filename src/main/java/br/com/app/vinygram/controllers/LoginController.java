package br.com.app.vinygram.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.app.vinygram.domains.Authorities;
import br.com.app.vinygram.domains.User;
import br.com.app.vinygram.security.AuthenticationFacade;
import br.com.app.vinygram.service.UserService;

@Controller
@RequestMapping("/")
public class LoginController implements ErrorController {
	
	@Autowired
	private UserService userService;
	
	
	
	
	
	@RequestMapping(value = "/login",method =  RequestMethod.GET)
	public @ResponseBody ModelAndView getLogin(ModelMap model){
		model.addAttribute("conteudo","/fragmentos/login");
		return new ModelAndView("/layouts/login/autenticar_layout",model);
	}
	
	@RequestMapping(value = "/loginError",method =  RequestMethod.GET)
	public @ResponseBody ModelAndView getLoginError(ModelMap model){

		model.addAttribute("conteudo","/fragmentos/login");
		return new ModelAndView("/layouts/login/autenticar_layout",model);
	}
	
	@RequestMapping(value = "/cadastro",method =  RequestMethod.GET)
	public @ResponseBody ModelAndView getCadastro(@ModelAttribute("user") User user, ModelMap model){
		model.addAttribute("conteudo","/fragmentos/cadastro");
		return new ModelAndView("/layouts/login/autenticar_layout",model);
	}
	
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(name =  "cpassword") String cpassword ,  ModelMap model, RedirectAttributes attr) {
		
		if(validaUser(user, cpassword, result).hasErrors()) {
	
			model.addAttribute("conteudo","/fragmentos/cadastro");
			return new ModelAndView("/layouts/login/autenticar_layout",model);
		}
		user.setPassword(AuthenticationFacade.criptografiaBase64Encoder(user.getPassword()));
		user.addAuths(Authorities.USER_ROLE);
		user = userService.save(user);
		
			if( user != null ) {
	
				attr.addFlashAttribute("success", "success");
				attr.addFlashAttribute("message", "Usuario salvo com sucesso.");
				return new ModelAndView("redirect:/cadastro");
				
			}else {
		
				attr.addFlashAttribute("error", "error");
				attr.addFlashAttribute("message", "Não foi possivel cadastrar o usuario.");
				return new ModelAndView("redirect:/cadastro");
			}
	}
	
	
	private BindingResult validaUser(User user,String cpassword ,BindingResult result) {
		
		if(this.userService.findByUsername(user.getUsername()) != null) {
			result.rejectValue("username", "Unique.user.username");
		}
		
		if(this.userService.findByEmail(user.getEmail()) != null) {
			result.rejectValue("email", "Unique.user.email");
		}
		
		if(!user.getPassword().equals(cpassword)) {
			result.rejectValue("password", "Conf.user.password");
		}
		
		return result;
	}
	
	
	
	
	
	@RequestMapping(value = "/error")
	public String handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	        if(statusCode == HttpStatus.FORBIDDEN.value()) {
	        	
	            return "errors/forbiden";
	        }else
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	        	
	            return "errors/not-found";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return "errors/internal-server";
	        }
	    }
	    return "error";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
}