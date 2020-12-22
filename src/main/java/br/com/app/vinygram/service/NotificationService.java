package br.com.app.vinygram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.app.vinygram.domains.Notification;
import br.com.app.vinygram.domains.User;
import br.com.app.vinygram.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private UserService userService;
	
	@Transactional(readOnly = false)
	public Notification save(Notification notification) {
		return notificationRepository.save(notification);
	}
	
	public Page<Notification> getNotification(int page) {
		
		System.out.println("pagina: "+page);
		page = page>0 ?page-1:page;	
		PageRequest pageRequest = PageRequest.of(page,20); 
		User user = userService.getUserAuthenticated();
		return notificationRepository.findByRecipientUserOrderByDateDesc(user, pageRequest);
	}
}
