package br.com.app.vinygram.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.app.vinygram.domains.User;
import br.com.app.vinygram.service.UserService;

@Service
public class CustomAutenticationProvider implements AuthenticationProvider, UserDetailsService{

	@Autowired
	private UserService userService; 
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		Authentication retornoAuthentication = null;
		String username = authentication.getName();
		String paString = AuthenticationFacade.criptografiaBase64Encoder(authentication.getCredentials().toString());
		
		User user = userService.findByUsernameAndPassword(username, paString);
		if(user != null) {
			retornoAuthentication  = new UsernamePasswordAuthenticationToken(user, new BCryptPasswordEncoder().encode(paString),user.getAuthorities());
		}
		return retornoAuthentication;
	}
	
	
	public static void updateUserAuthenticated(User userUpdate) {
		
		if(userUpdate != null) {
			 Authentication auth = authenticateUpdate(userUpdate);
			    SecurityContext sc = SecurityContextHolder.getContext();
			    sc.setAuthentication(auth);
		}
	}
	
	
	private static Authentication authenticateUpdate(User userUpdate) throws AuthenticationException {
		
		//String username = userUpdate.getUsername();
		String paString = userUpdate.getPassword();
		Authentication auth = new UsernamePasswordAuthenticationToken(userUpdate, new BCryptPasswordEncoder().encode(paString),userUpdate.getAuthorities());
		
		return auth ;
	}

	@Override
	public boolean supports(Class<?> authentication) {
	
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	 
	 @Bean
	public PasswordEncoder passwordEncoder() {
	       
		 return new BCryptPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return new User();
	}
	
}