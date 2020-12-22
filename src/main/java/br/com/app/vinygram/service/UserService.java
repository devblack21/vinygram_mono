package br.com.app.vinygram.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.app.vinygram.domains.Notification;
import br.com.app.vinygram.domains.Operation;
import br.com.app.vinygram.domains.User;
import br.com.app.vinygram.repository.UserRepository;
import br.com.app.vinygram.security.AuthenticationFacade;

@Qualifier("userService")
@Service
@Transactional(readOnly = true,  propagation = Propagation.REQUIRED)
public class UserService  {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NotificationService notificationService;
	
	@Transactional(readOnly = false)
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public User findCompleteByUsername(String username) {
		return userRepository.findCompleteUserByUsername(username);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}	
	
	public User findByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword( username,  password);
	}
	
	public int followingsByUser(long id) {
		return userRepository.followingsByUser(id);
	}
	
	public int followsByUser(long id) {
		return userRepository.followsByUser(id);
	}
	
	public List<User> findTop10ByOrderByUsernameContaining(String username){
		return userRepository.findTop10ByUsernameContaining(username);
	}
	
	public User findUserFollowingUser(long idUser, long idFollowing) {
		return userRepository.findUserFollowingUser(idUser, idFollowing);
	}
	
	public User findById(long id) {
		return userRepository.findById(id).get();
	}
	
	public User getUserAuthenticated() {
		
		return userRepository.findByUsername(AuthenticationFacade.getAuthenticated().getName());
	}
	
	@Transactional(readOnly = false)
	public void followOrUnfollow(long idUserFollow) {
		
		User userAuthenticated = getUserAuthenticated();
		Notification notify = null;
		if(userAuthenticated != null) {
			
			User userAuthenticatedFollow = this.findCompleteByUsername(userAuthenticated.getUsername());
			userAuthenticatedFollow = (userAuthenticatedFollow!=null) ? userAuthenticatedFollow : userAuthenticated ;
			User userFollowing = this.findById(idUserFollow);
	
			if(userFollowing != null) {
				
				if (findUserFollowingUser(userAuthenticated.getId(), userFollowing.getId()) != null) {
					//unfollow
						userAuthenticatedFollow.removeFollowing(userFollowing);
						
				}else {
					//follow
						userAuthenticatedFollow.addFollowing(userFollowing);	
						
						notify = new Notification();
						notify.setOperation(Operation.FOLLOW);
						notify.setRecipientUser(userFollowing);
						notify.setSenderUser(userAuthenticatedFollow);
						notify.setDate(LocalDateTime.now());
					
				}
				
			if(this.save(userAuthenticatedFollow) != null) {
				if(notify != null) {
					notificationService.save(notify);
				}
				
			}
			
		
			}
			
		}
	}
	
}