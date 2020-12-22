package br.com.app.vinygram.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import br.com.app.vinygram.domains.PostEntity;
import br.com.app.vinygram.domains.User;

@Aspect
@Component
public class CloundImagemAspect extends CloundinaryAsync {

	private static final String USERS_FOLDER = "users";
	private static final String POSTS_FOLDER = "posts";
	

	
	@Async
	@After("execution(* br.com.app.vinygram.controllers.PostController.saveImage(..) )")
	public void savePost(JoinPoint pjp) {
		
				System.out.println("save image");
				BindingResult result = null;
				MultipartFile multipartFile = null;
				String name = null;
				Thread uploadImageThread = uploadImage(POSTS_FOLDER);

				for (Object object : pjp.getArgs()) {

					if (object instanceof PostEntity) {
					
						name = String.valueOf(((PostEntity) object).getId());
						
					}

					if (object instanceof MultipartFile) {
						multipartFile = (MultipartFile) object;
					}
					
					if (object instanceof BindingResult) {
						result = (BindingResult) object;
					}
				}
				if(!result.hasErrors()) {
					configurar(multipartFile, name);
					uploadImageThread.start();
				}	
	}
	
	@Async
	@After("execution(* br.com.app.vinygram.controllers.UserController.save(..) )")
	public void save(JoinPoint pjp) {
		
				
				BindingResult result = null;
				MultipartFile multipartFile = null;
				String name = null;
				Thread uploadImageThread = uploadImage(USERS_FOLDER);

				for (Object object : pjp.getArgs()) {

					if (object instanceof User) {
						name = String.valueOf(((User) object).getId());
					
					}

					if (object instanceof MultipartFile) {
						multipartFile = (MultipartFile) object;
					}
					
					if (object instanceof BindingResult) {
						result = (BindingResult) object;
					}
				}
				if(!result.hasErrors()) {
					configurar(multipartFile, name);
					uploadImageThread.start();
				}	
	}
	
	@After("execution(* br.com.app.vinygram.controllers.PostController.updateImage(..) )")
	public void updatePost(JoinPoint pjp) throws InterruptedException {
		
		BindingResult result = null;
		MultipartFile multipartFile = null;
		String name = null;
	
		Thread uploadImageThread = uploadImage(POSTS_FOLDER);
	
		for (Object object : pjp.getArgs()) {

			if (object instanceof PostEntity) {

				name = String.valueOf(((PostEntity) object).getId());

			}

			if (object instanceof MultipartFile) {

				multipartFile = (MultipartFile) object;
			}
			if (object instanceof BindingResult) {
				result = (BindingResult) object;
			}
		}
		if(!result.hasErrors() && !multipartFile.isEmpty() && multipartFile != null) {
			Thread deleteThread = removeAsync(POSTS_FOLDER, name);
			deleteThread.start();
			deleteThread.join();
			configurar(multipartFile, name);
			uploadImageThread.start();
		}
		
	}
	
	@After("execution(* br.com.app.vinygram.controllers.UserController.update(..) )")
	public void update(JoinPoint pjp) throws InterruptedException {
		
		BindingResult result = null;
		MultipartFile multipartFile = null;
		String name = null;
	
		Thread uploadImageThread = uploadImage(USERS_FOLDER);
	
		for (Object object : pjp.getArgs()) {

			if (object instanceof User) {

				name = String.valueOf(((User) object).getId());

			}

			if (object instanceof MultipartFile) {

				multipartFile = (MultipartFile) object;
			}
			if (object instanceof BindingResult) {
				result = (BindingResult) object;
			}
		}
		if(!result.hasErrors() && !multipartFile.isEmpty() && multipartFile != null) {
			Thread deleteThread = removeAsync(USERS_FOLDER, name);
			deleteThread.start();
			deleteThread.join();
			configurar(multipartFile, name);
			uploadImageThread.start();
		}
		
	}
	@After("execution(* br.com.app.vinygram.controllers.UserController.remove(..) )")
	public void deletar() {
		
	}

	private void configurar(MultipartFile multipartFile, String name) {
		this.setMultipartFile(multipartFile);
		this.setName(name);
	}

}
