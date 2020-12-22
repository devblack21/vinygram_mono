package br.com.app.vinygram.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.app.vinygram.domains.PostEntity;
import br.com.app.vinygram.domains.PostVideoEmbedEntity;
import br.com.app.vinygram.domains.User;
import br.com.app.vinygram.repository.PostRepository;
import br.com.app.vinygram.repository.PostVideoRepository;

@Service
@Transactional(readOnly = true,  propagation = Propagation.REQUIRED)
public class PostService {

	@SuppressWarnings("unused")
	private final int SIZE_PAGE = 10;
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	PostVideoRepository postVideoRepository;
	
	@Transactional(readOnly = false)
	public PostEntity save(PostEntity post){
		//post.setPostDate(LocalDateTime.now(ZoneId.of("America/Sao_paulo")));
		return postRepository.save(post);
	}
	
	@Transactional(readOnly = false)
	public PostVideoEmbedEntity save(PostVideoEmbedEntity post){
		return postVideoRepository.save(post);
	}
	
	public List<PostEntity> getList(){
		
		return postRepository.findAll();
		
	}
	
	public List<PostEntity> getPaginatedPostsFromUserNotFollowing(int page,long idUser){
		
		page = page>0 ?page-1:page;		
		int size = 10;
		return postRepository.findByUserOrderByPostDateDesc(idUser,page*size,size);
	}
	
	public List<PostEntity> getPaginatedPostsFromUserProfile(int page,long idUser){
		
		page = page>0 ?page-1:page;		
		int size = 10;
		return postRepository.paginatedPostsByUserProfile(idUser,page*size,size);
	}
	
	

	public List<PostEntity> getPaginatedPostsFromUser(int page,long idUser){
		
		page = page>0 ?page-1:page;		
		int size = 10;
		return postRepository.paginatedPostsByUser(idUser,page*size,size);
	}
	
	public Page<PostEntity> getPostsEntityFromUser(User user, int page){
		int size = 10;
		PageRequest pageRequest = PageRequest.of(page,size); 
		return postRepository.findByUserOrderByPostDateDesc(user, pageRequest);
	}
	
	public PostEntity findById(long id) {
		return postRepository.findById(id).get();
	}
	

	public PostEntity remove(PostEntity post){
		 postRepository.delete(post);
		 return post;
	}
	
}