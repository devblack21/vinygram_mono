package br.com.app.vinygram.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.app.vinygram.domains.Notification;
import br.com.app.vinygram.domains.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	Page<Notification> findByRecipientUserOrderByDateDesc(User user, Pageable pageable);

}
