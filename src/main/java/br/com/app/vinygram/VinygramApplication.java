package br.com.app.vinygram;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import br.com.app.vinygram.config.WebConfig;
import br.com.app.vinygram.exception.RestExceptionHandler;
import br.com.app.vinygram.security.WebSecurityConfig;

@ServletComponentScan(basePackageClasses = {WebConfig.class, WebSecurityConfig.class, LocalDateTime.class, LocalDate.class})
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaRepositories("br.com.app.vinygram.repository")
@EnableTransactionManagement
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class VinygramApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(VinygramApplication.class, args);
		
	}

}