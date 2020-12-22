package br.com.app.vinygram.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;


@Configuration
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;

	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheable(true);
		return templateResolver;
	}
	
	
	
	@Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }
 
 
	@Bean
	public ViewResolver viewResolver() {
	    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	    ViewResolverRegistry registry = new ViewResolverRegistry(null, applicationContext);
	    viewResolver.setTemplateEngine(templateEngine());
        registry.viewResolver(viewResolver);
	    return viewResolver;
	}
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
	
	
	/*@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new CategoriaVeiculoConverter());
	
	}*/

	@Bean
	public MessageSource messageSource() {

		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("messages");

		return source;

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/static/**");
	}

}