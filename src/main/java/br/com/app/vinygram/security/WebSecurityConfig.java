package br.com.app.vinygram.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan("br.com.app.vinygram.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAutenticationProvider authProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.and().sessionManagement().maximumSessions(1).expiredUrl("/login?invalidate")
		.and().invalidSessionUrl("/login?in")
		.and().sessionManagement().sessionFixation()
		  .migrateSession().and()
		  .csrf().disable().authorizeRequests()
		
		.antMatchers(HttpMethod.GET,"/login").permitAll()
		.antMatchers(HttpMethod.GET,"/cadastro").permitAll()
		.antMatchers(HttpMethod.POST,"/save").permitAll()
		
		.antMatchers("/post").hasRole("USER_ROLE")
		
		.anyRequest().authenticated()
		
		.and().httpBasic()
		.and().formLogin().loginPage("/login").defaultSuccessUrl("/post/feed").permitAll()
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).deleteCookies("JSESSIONID")
       .and().rememberMe().tokenValiditySeconds(5000).rememberMeParameter("remember-me").rememberMeCookieName("remember-me-cookie").alwaysRemember(true).
       userDetailsService(authProvider);
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authProvider).eraseCredentials(false).getDefaultUserDetailsService();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**");
	}
	
}