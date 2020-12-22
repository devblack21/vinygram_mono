package br.com.app.vinygram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import br.com.app.vinygram.domains.Notification;
import br.com.app.vinygram.service.NotificationService;
import br.com.app.vinygram.service.UserService;

@Controller
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/list")
	public ModelAndView getNotifications(  ModelMap model) {
		
		model.addAttribute("user",userService.getUserAuthenticated());
		model.addAttribute("notifications",notificationService.getNotification(1));
		model.addAttribute("conteudo", "layouts/autenticado/notification_layout");
		
		return new ModelAndView("layouts/autenticado/noti_layout_padrao", model);	
	}
	
	@RequestMapping("/listscroll")
	public ModelAndView getNotificationsPage(@RequestParam(name = "page", required = true) int page,  ModelMap model) {
		ModelAndView modelAndView = null;
		//model.addAttribute("user",user);

		Page<Notification> lista = notificationService.getNotification(page);
		System.out.println(lista.getNumberOfElements());
		if(lista.getNumberOfElements() > 0) {
	
			model.addAttribute("notifications",lista);
			modelAndView = new ModelAndView("fragmentos/autenticado/feed/notifyMedia", model);
		}
		
		return 	modelAndView;
	}
	
	
}
