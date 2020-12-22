package br.com.app.vinygram.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.app.vinygram.domains.PostEntity;
import br.com.app.vinygram.domains.User;
import br.com.app.vinygram.security.AuthenticationFacade;
import br.com.app.vinygram.security.CustomAutenticationProvider;
import br.com.app.vinygram.service.PostService;
import br.com.app.vinygram.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	private ModelAndView profileModelAndView(User user , ModelMap model, boolean refresh) throws IOException {
		
			User userAuthenticated = userService.getUserAuthenticated();
			ModelAndView modelAndView = null;
			
			
			
			if(user != null) {
				
				model.addAttribute("user", user);
				int seguindo = userService.followsByUser(user.getId());
				int seguidores = userService.followingsByUser(user.getId());
				StringBuilder sb = new StringBuilder();
				sb.append("Seguindo ").append(seguindo).append(" pessoas");
				model.addAttribute("follows",sb.toString());
				sb = new StringBuilder();
				sb.append(seguidores).append( (seguidores==1) ? " seguidor" : " seguidores"); 
				model.addAttribute("followings",sb.toString());
				model.addAttribute("imgURL",urlImage(user.getId()));
				 
				if (user.getUsername().equals(userAuthenticated.getUsername())) {
					model.addAttribute("isOtherUser", false);
					
				}else {
					model.addAttribute("isOtherUser", true);
					if(userService.findUserFollowingUser(userAuthenticated.getId(), user.getId()) != null) {
						
						model.addAttribute("follow", false);
						 
					 }else {
						 model.addAttribute("follow", true);
					 }
				}
		}
		
		if(!refresh) {
			List<PostEntity> list = new ArrayList<>();
			list = postService.getPaginatedPostsFromUserProfile(1,user.getId());
			if(list.isEmpty()) {
				
				
				list = postService.getPaginatedPostsFromUserNotFollowing(1,user.getId());
				
			}
			
			model.addAttribute("postsList",list);
			model.addAttribute("posts","layouts/autenticado/postProfile_layout");
			model.addAttribute("conteudo", "layouts/sub_layouts/perfil_subLayout");
			
			modelAndView = new ModelAndView("layouts/autenticado/feed_layout",model);
			 
		}else {
			modelAndView = new ModelAndView("/fragmentos/autenticado/perfil/perfil_card :: perfil",model);
		}	
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/profile",method = RequestMethod.GET)
	public @ResponseBody ModelAndView getProfile(@ModelAttribute("user") User user,@ModelAttribute("post") PostEntity post,ModelMap model) throws IOException {
		model.addAttribute("post",post);
		User userAuthenticated =  userService.getUserAuthenticated();
		return profileModelAndView(userAuthenticated,model,false);
	}
	
	
	@RequestMapping(value = "/profileSearch",method = RequestMethod.GET)
	public @ResponseBody ModelAndView getProfileSearch(@ModelAttribute("user") User user,@ModelAttribute("post") PostEntity post,@RequestParam("pesquisa") String username, ModelMap model) throws IOException {

		User userSearch = userService.findByUsername(username);
		
		return profileModelAndView(userSearch, model, false);
	}
	
	@RequestMapping(value = "/profileRefresh",method = RequestMethod.GET)
	public @ResponseBody ModelAndView getProfileRefresh(@ModelAttribute("user") User user,@ModelAttribute("post") PostEntity post,@RequestParam("idProfile") long id, ModelMap model) throws IOException {
		
		User userSearch = userService.findById(id);
		return profileModelAndView(userSearch, model, true);
	}
	
	@RequestMapping(value = "/followOrUnfollow", method = RequestMethod.GET)
	public @ResponseBody void followOrUnfollow(ModelMap model, @RequestParam("idUserFollow") long idUserFollow) {
		if(idUserFollow > 0) {
			userService.followOrUnfollow(idUserFollow);
		}
	}
	
	@RequestMapping(value = "/users",method = RequestMethod.GET)
	public @ResponseBody List<String> listUsers(@RequestParam("username") String username)
	{
		List<String> lista = new ArrayList<String>();
		if(username != null && !username.isEmpty()) {
			for (User user : userService.findTop10ByOrderByUsernameContaining(username)) {
				lista.add(user.getUsername());
			}
		}
		return lista;	
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam("upload") MultipartFile multipartFile, @RequestParam("cpassword") String cpassword, ModelMap model, RedirectAttributes attr) {
		
		User retornoUser = userService.getUserAuthenticated();
		
		result = validaUser(user, retornoUser.getEmail(),multipartFile,cpassword, result);
		
		if(result.hasErrors()) {
			System.out.println("erro ao salvar! "+result.getAllErrors());
			return new ModelAndView("redirect:/user/profile");
		}
		
		user.setId(retornoUser.getId());
		User retornoFollowings = userService.findCompleteByUsername(retornoUser.getUsername());
		if(retornoFollowings != null) {
			user.setFollowing(retornoUser.getFollowing());
		}
		
		user.setPassword(AuthenticationFacade.criptografiaBase64Encoder(user.getPassword()));
		user = userService.save(user);
		
		if( user != null ) {
			
			attr.addFlashAttribute("success", "success");
			attr.addFlashAttribute("message", "Usuario salvo com sucesso.");
			System.out.println("salvo!");
			//atualizar o usuario autenticado
			CustomAutenticationProvider.updateUserAuthenticated(user);
			return new ModelAndView("redirect:/user/profile");
			
		}else {
			System.out.println("não salvo!");
			attr.addFlashAttribute("error", "error");
			attr.addFlashAttribute("message", "Não foi possivel cadastrar o usuario.");
			return new ModelAndView("redirect:/user/profile");
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute("user") User user,  ModelMap model) {
		
		return new ModelAndView("redirect:/user/profile");
	}
	
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ModelAndView remove(@ModelAttribute("user") User user,  ModelMap model) {
		
		return new ModelAndView("redirect:/user/profile");
	}
	
	
	public BindingResult validaUser(User user,String email,MultipartFile multipartFile,String cpassword, BindingResult result) {
	
		if( ((this.userService.findByUsername(user.getUsername()) != null) && !AuthenticationFacade.getAuthenticated().getName().equals(user.getUsername())) ) {
				
			result.rejectValue("username", "Unique.user.username");
		}
		
		if(this.userService.findByEmail(user.getEmail()) != null && !user.getEmail().equals(email)) {
			result.rejectValue("email", "Unique.user.email");
		}
		
		if(!user.getPassword().equals(cpassword)) {
			result.rejectValue("password", "Conf.user.password");
		}
		
		if (multipartFile.getSize() >= 131072) 
		{
			result.rejectValue("urlImage", "Size.user.urlImage");
		}
		
		return result;
	}
	
	private String urlImage(long id ) throws IOException {
		String URL = "https://res.cloudinary.com/devblack/image/upload/users/";
		StringBuffer sBuffer = new StringBuffer();
		
		sBuffer.append(URL).append(id).append(".jpg");
		URL url = new URL(sBuffer.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();            
		connection.addRequestProperty("Request-Method","GET");      
		connection.setDoInput(true);    
		connection.setDoOutput(false);    
		connection.connect();   
		String retorno = "https://res.cloudinary.com/devblack/image/upload/users/sem-foto.jpg";

		if(connection.getResponseCode() == 200) {
			retorno = sBuffer.toString();
		}  
		return retorno;
	}
}