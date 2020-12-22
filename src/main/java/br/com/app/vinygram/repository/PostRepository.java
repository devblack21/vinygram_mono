package br.com.app.vinygram.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.app.vinygram.domains.PostEntity;
import br.com.app.vinygram.domains.User;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

	Page<PostEntity> findByUserOrderByPostDateDesc(User user, Pageable pageable);
	
	@Query(value = "select DISTINCT posts.id_post, posts.title, posts.subtitle, posts.post_date, posts.user_id_user from users "
			+ "join posts  on users.id_user = posts.user_id_user join user_following on users.id_user = user_following.following_id_user or users.id_user = user_following.id_user where user_following.id_user = ?1 "
			+ "order by posts.post_date DESC limit ?2,?3",
			nativeQuery = true)
	List<PostEntity> paginatedPostsByUser(long idUser,long page, long size);
	
	
	@Query(value = "select DISTINCT posts.id_post, posts.title, posts.subtitle, posts.post_date, posts.user_id_user from users "
			+ "join posts  on users.id_user = posts.user_id_user join user_following on users.id_user = user_following.id_user where user_following.id_user = ?1 "
			+ "order by posts.post_date DESC limit ?2,?3",
			nativeQuery = true)
	List<PostEntity> paginatedPostsByUserProfile(long idUser,long page, long size);
	
	
	@Query(value = "select DISTINCT posts.id_post, posts.title, posts.subtitle, posts.post_date, posts.user_id_user from posts where posts.user_id_user = ?1 order by posts.post_date limit ?2,?3",
			nativeQuery = true)
	List<PostEntity> findByUserOrderByPostDateDesc(long idUser,long page, long size);
	
	
	
}