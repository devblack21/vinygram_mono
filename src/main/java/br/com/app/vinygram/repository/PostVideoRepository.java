package br.com.app.vinygram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.app.vinygram.domains.PostVideoEmbedEntity;

@Repository
public interface PostVideoRepository extends JpaRepository<PostVideoEmbedEntity, Long> {
	
	
	 
	
}