package br.com.app.vinygram.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
import br.com.app.vinygram.domains.Notification;
import br.com.app.vinygram.domains.Operation;
import br.com.app.vinygram.domains.PostEntity;
import br.com.app.vinygram.domains.PostVideoEmbedEntity;
import br.com.app.vinygram.security.AuthenticationFacade;
import br.com.app.vinygram.service.NotificationService;
import br.com.app.vinygram.service.PostService;
import br.com.app.vinygram.service.UserService;

@Controller
@RequestMapping("post")
public class PostController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping(value = "/feed", method = RequestMethod.GET)
	public @ResponseBody ModelAndView getFeed(@ModelAttribute("post") PostEntity post,  ModelMap model) {
		
		model.addAttribute("post",post);
		model.addAttribute("username",AuthenticationFacade.getAuthenticated().getName().toString());
		model.addAttribute("user",userService.getUserAuthenticated());
		List<PostEntity> list = new ArrayList<>();
		list = postService.getPaginatedPostsFromUser(1, userService.getUserAuthenticated().getId());
		if(list.isEmpty()) {
			
			list = postService.getPaginatedPostsFromUserNotFollowing(1,userService.getUserAuthenticated().getId());
			
		}		
		
		model.addAttribute("postsList",list);
		model.addAttribute("conteudo", "layouts/autenticado/postFeed_layout");
		
		return new ModelAndView("layouts/autenticado/feed_layout", model);
		
	}
	
	@RequestMapping(value = "/feedlist", method = RequestMethod.GET)
	public @ResponseBody ModelAndView getFeedList(@RequestParam("user") long user,@RequestParam(name = "page", required = true) int page,@RequestParam(name = "profile", required = true) boolean profile, ModelMap model) {
		
		ModelAndView modelAndView = null;
	
		model.addAttribute("username",AuthenticationFacade.getAuthenticated().getName().toString());
		List<PostEntity> list = new ArrayList<>();
		if(page >= 0) {
			
			if(user <= 0) {
				user = userService.getUserAuthenticated().getId();
			}
			if(profile) {
				System.out.println("profile");
				list = postService.getPaginatedPostsFromUserProfile(page, user);
			}else {
				System.out.println("no profile");
				list = postService.getPaginatedPostsFromUser(page, user);
			}
			
			
			
			model.addAttribute("postsList",list);
		}
		
		if(list.size() > 0) {
			modelAndView = new ModelAndView("fragmentos/autenticado/feed/postlist",model);
		}
		
		return  modelAndView;
	}
	
	@RequestMapping(value = "/likeOrDeslike", method = RequestMethod.GET)
	public @ResponseBody String likeOrDeslike(ModelMap model, @RequestParam("idPostLike") long idUserFollow) {
	
		String retornoString = "nenhuma curtida.";
		if(idUserFollow > 0) {
			PostEntity postEntity = postService.findById(idUserFollow);
			
			if(postEntity != null) {
				
				postEntity.refreshLike(userService.getUserAuthenticated());
				postEntity = postService.save(postEntity);
				
				if(postEntity.getLikes().contains(userService.getUserAuthenticated())) {
					
					
					Notification notify = new Notification();
					notify.setOperation(Operation.LIKE);
					notify.setRecipientUser(postEntity.getUser());
					notify.setSenderUser(userService.getUserAuthenticated());
					notify.setDate(LocalDateTime.now());
					notificationService.save(notify);
				}
				
				retornoString = postEntity.getLikesString();
			}
			
		}
		return retornoString;
	}
	
	@RequestMapping(value = "/saveText", method = RequestMethod.POST)
	public @ResponseBody ModelAndView saveText(@Valid @ModelAttribute("post") PostEntity postEntity,BindingResult result,ModelMap model, RedirectAttributes attr) {
		
		postEntity.setUser(userService.getUserAuthenticated());
		postEntity.setPostDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
		if(validPost(result, postEntity).hasErrors()) {
			return new ModelAndView("redirect:/#");
		}
		
		PostEntity retornoEntity = postService.save(postEntity);
		
		if( retornoEntity != null ) {
			
			attr.addFlashAttribute("success", "success");
			attr.addFlashAttribute("message", "Post salvo com sucesso.");
			System.out.println("salvo!");
			//atualizar o usuario autenticado
			return new ModelAndView("redirect:/post/feed");
			
		}else {
			System.out.println("não salvo!");
			attr.addFlashAttribute("error", "error");
			attr.addFlashAttribute("message", "Não foi possivel postar.");
			return new ModelAndView("redirect:/post/feed");
		}
	}
	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public @ResponseBody ModelAndView saveImage(@Valid @ModelAttribute("post") PostEntity postEntity,BindingResult result,@RequestParam(name = "upload_image") MultipartFile multipartFile,ModelMap model, RedirectAttributes attr) {
		postEntity.setUser(userService.getUserAuthenticated());
		postEntity.setPostDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
		if(validPostImage(result, multipartFile, postEntity).hasErrors()) {
			return new ModelAndView("redirect:/#");
		}
		

		PostEntity retornoEntity = postService.save(postEntity);
		
		if( retornoEntity != null ) {
			
			attr.addFlashAttribute("success", "success");
			attr.addFlashAttribute("message", "Post salvo com sucesso.");
			System.out.println("salvo!");
			//atualizar o usuario autenticado
			return new ModelAndView("redirect:/post/feed");
			
		}else {
			System.out.println("não salvo!");
			attr.addFlashAttribute("error", "error");
			attr.addFlashAttribute("message", "Não foi possivel postar.");
			return new ModelAndView("redirect:/post/feed");
		}
	
	}
	@RequestMapping(value = "/saveVideo", method = RequestMethod.POST)
	public @ResponseBody ModelAndView saveVideo(@Valid @ModelAttribute("post") PostEntity postEntity ,BindingResult result,@RequestParam(name = "site", required = true) String site,@RequestParam(name = "codigoVideo", required = true) String codigoVideo, ModelMap model, RedirectAttributes attr) {
		postEntity.setUser(userService.getUserAuthenticated());
		postEntity.setPostDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
		if(validPost(result, postEntity).hasErrors()) {
			return new ModelAndView("redirect:/#");
		}
		
		PostVideoEmbedEntity postVideoEmbedEntity = new PostVideoEmbedEntity();	
		postVideoEmbedEntity.setSite(site);
		postVideoEmbedEntity.setCodVideoEmbed(codigoVideo);
		
		if(validPostVideo(result, postVideoEmbedEntity).hasErrors()) {
			return new ModelAndView("redirect:/#");
		}
	
		//postEntity.setPostVideo(postVideoEmbedEntity);
		postVideoEmbedEntity.setPost(postEntity);
		
		PostVideoEmbedEntity retornoEntity = postService.save(postVideoEmbedEntity);
		
		if( retornoEntity != null ) {
			
			attr.addFlashAttribute("success", "success");
			attr.addFlashAttribute("message", "Post salvo com sucesso.");
			System.out.println("salvo!");
			//atualizar o usuario autenticado
			return new ModelAndView("redirect:/post/feed");
			
		}else {
			System.out.println("não salvo!");
			attr.addFlashAttribute("error", "error");
			attr.addFlashAttribute("message", "Não foi possivel postar.");
			return new ModelAndView("redirect:/post/feed");
		}
		

	}
	
	


	@RequestMapping(value = "/list")
	public @ResponseBody void list() {
		//StringBuilder sb = new StringBuilder();
		/*for(PostEntity post : postService.getList()) {
			sb.append("Titulo: ").append(post.getTitle()).append("\n");
			sb.append("Data: ").append(formatDate(post.getPostDate())).append("\n");
			sb.append("Site: ").append(post.getPostVideo() != null ? post.getPostVideo().getSite():"");
			sb.append("\nLikes: ").append(post.getLikes().size()).append("\n");
		}
		System.out.println();
		System.out.println(sb.toString());*/
		
	}
	
	
	@RequestMapping(value = "like")
	public @ResponseBody ModelAndView likeOrDislike(@RequestParam(value = "post" ,required = true) long idPost, @RequestParam("page") int page, ModelMap model) {
		
		if(idPost > 0) {
			userService.followOrUnfollow(idPost);
		}
		return null;
	}

	
	public BindingResult validPost(BindingResult result, PostEntity post) {
		
		return result;	
	}
	
	public BindingResult validPostImage(BindingResult result, MultipartFile multipartFile, PostEntity post) {
		if (multipartFile.getSize() >= 131072) 
		{
			result.rejectValue("urlImage", "Size.post.urlImage");
		}
		return result;	
	}
	
	public BindingResult validPostVideo(BindingResult result, PostVideoEmbedEntity post) {
		if(post.getSite().isEmpty()) {
			result.rejectValue("site", "NotEmpty.PostVideoEmbedEntity.site");
		}
		
		if(post.getCodVideoEmbed().isEmpty()) {
			result.rejectValue("codVideoEmbed", "NotEmpty.PostVideoEmbedEntity.codVideoEmbed");
		}
		return result;	
	}
}