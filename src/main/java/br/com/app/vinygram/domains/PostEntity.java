package br.com.app.vinygram.domains;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import com.sun.istack.NotNull;
import br.com.app.vinygram.security.AuthenticationFacade;
import br.com.app.vinygram.utils.DateDiferencesPosts;

@Entity
@Table(name = "Posts")
public class PostEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_POST", nullable = false)
	private long id;
	@NotNull
	@NotEmpty
	@Column(name = "TITLE", nullable = false, length = 30)
	private String title;
	@NotEmpty
	@NotNull
	@Column(name = "SUBTITLE")
	private String subtitle;
	
	@NotNull
	@OneToOne(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
	@JoinColumn(columnDefinition = "ID_USER", referencedColumnName = "ID_USER")
	private User user;
	
	@OneToOne(mappedBy = "post", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH })
	private PostVideoEmbedEntity postVideo;
	
	
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE,CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "POST_LIKES" , joinColumns = @JoinColumn(columnDefinition = "USER"))
	@PrimaryKeyJoinColumn
	private List<User> likes = new ArrayList<User>();
	
	@NotNull
	@Column(name = "POST_DATE", nullable = false)
	private LocalDateTime postDate;
	
	@Transient
	private String dateString;
	
	@Transient
	private String likesString;
	
	@Transient
	private String urlImage;
	
	public long getId() {
		return id;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	
	public String getTitle() {
		return title;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public PostVideoEmbedEntity getPostVideo() {
		return postVideo;
	}
	
	public void setPostVideo(PostVideoEmbedEntity postVideo) {
		this.postVideo = postVideo;
	}
	
	public List<User> getLikes() {
		return likes;
	}
	
	public void setLikes(List<User> likes) {
		this.likes = likes;
	}
	
	public void refreshLike(User user) {
		if(this.likes.contains(user)) {
			removeLike(user);
		}else {
			addLike(user);
		}
	}
	
	public void removeLike(User user) {
		if(this.likes.contains(user)) {
			this.likes.remove(user);
		}	
	}
	
	public void addLike(User user) {
		if(!this.likes.contains(user)) {
			this.likes.add(user);
		}	
	}
	
	public boolean containsLike() {
		
		return this.likes.contains(AuthenticationFacade.getAuthenticated().getPrincipal());
	}
	
	
	public boolean containsLike(User user) {
		return this.likes.contains(user);
	}
	
	public String getLikesString() {
		String retornoString = "";
		if(this.likes.size() > 0) {
			retornoString = (this.likes.size() == 1)? "uma pessoa curtiu isso.": this.likes.size()+" pessoas curtiram.";
		}else {
			retornoString = "nenhuma pessoa curtiu isso.";
		}
		this.likesString = retornoString;
		return likesString;
	}
	
	public LocalDateTime getPostDate() {
		return postDate;
	}


	public String getDateString() {
		this.dateString = formatDate(this.postDate);
		return this.dateString;
	}
	
	public void setPostDate(LocalDateTime postDate) {
		this.postDate = postDate;
	}
	
	
	private String formatDate(LocalDateTime localDateTime) {
	
		return DateDiferencesPosts.getBuider().addDataDatePostTime(this.postDate).verifyDiferencesDate().build().toString();
	}
	
	public String getUrlImage() throws IOException {
		String URL = "https://res.cloudinary.com/devblack/image/upload/v1/posts/";
		StringBuffer sBuffer = new StringBuffer();
		
		sBuffer.append(URL).append(id).append(".jpg");
		URL url = new URL(sBuffer.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();            
		connection.addRequestProperty("Request-Method","GET");      
		connection.setDoInput(true);    
		connection.setDoOutput(false);    
		connection.connect();   
		String retorno = null;

		if(connection.getResponseCode() == 200) {
			retorno = sBuffer.toString();
		}  
		return retorno;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostEntity other = (PostEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}