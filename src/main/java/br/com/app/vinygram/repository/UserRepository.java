package br.com.app.vinygram.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.app.vinygram.domains.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	
	User findByUsernameAndPassword(String username, String password);
	
	@Query(value = "select count(*) from user_following where id_user = ?1 ", nativeQuery = true)
	int followsByUser(long id);
	
	@Query(value = "select count(*) from user_following where following_id_user = ?1 ", nativeQuery = true)
	int followingsByUser(long id);
	
	@Query(value = "select f.following_id_user from user_following f where f.id_user = ?1", nativeQuery = true)
	List<User> findFollowsByUser(long id);
	
	@Query(value = "select f.following_id_user from user_following where following_id_user = ?1", nativeQuery = true)
	List<User> findFollowingsByUser(long id);
	
	@Query(value = "select * from user_following join users on user_following.id_user = users.id_user where users.username = ?1", nativeQuery = true)
	User findCompleteUserByUsername(String username);

	List<User> findTop10ByUsernameContaining(String username);
	
	@Query(value = "select * from user_following join users on user_following.id_user = users.id_user where user_following.id_user = ?1 and user_following.following_id_user = ?2", nativeQuery = true)
	User findUserFollowingUser(long idUser, long idFollowing);
	
}
