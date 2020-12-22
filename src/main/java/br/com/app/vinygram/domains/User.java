package br.com.app.vinygram.domains;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"USERNAME","EMAIL"})})
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_USER")
	private long id;
	@NotEmpty
	@NotNull
	@Column(name = "USERNAME",length = 20,  unique = true, nullable = false)
	private String username;
	@NotNull
	@NotEmpty
	@Column(name = "PASSWORD")
	private String password;
	@NotNull
	@NotEmpty
	@Email
	@Column(name = "EMAIL", length = 30,  unique = true, nullable = false)
	private String email;
	@Column(name = "BIO")
	private String bio;
	@NotNull
	@NotEmpty
	@Column(name = "FIRSTNAME",nullable = false)
	private String firstName;
	@NotNull
	@NotEmpty
	@Column(name = "LASTNAME",nullable = false)
	private String lastName;
	
	@ManyToMany(cascade = {CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinTable(name = "USER_FOLLOWING", joinColumns = @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER"), inverseJoinColumns = @JoinColumn(name= "following_id_user"))
	private List<User> following;
	
	@ElementCollection(targetClass = Authorities.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "USER_AUTHORITIES", 
	joinColumns = @JoinColumn(name =  "ID_USER"))
	@Enumerated(EnumType.STRING)
	private List<Authorities> auths = new ArrayList<Authorities>();
	@Transient
	private String urlImage;
	
	public User() {
		
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void removeFollowing(User user) {
		if(user!=null && this.following.contains(user)) {
			this.following.remove(user);
		}
	}
	
	public List<User> getFollowing() {
		return following;
	}
	
	public void setFollowing(List<User> following) {
		this.following = following;
	}
	
	public void addFollowing(User user) {
		this.following.add(user);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addAuths(Authorities authorities) {
		this.auths.add(authorities);
	}
	
	public void setAuthorities(List<Authorities> authorities) {
		this.auths = null;
	}
	
	public List<Authorities> getAuths() {
		return auths;
	}

	
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		
		for(Authorities role : this.auths) {
			list.add(new SimpleGrantedAuthority(role.toString()));
		}
		
		return list;
	}

	public String getUrlImage() throws IOException {
		
		String URL = "https://res.cloudinary.com/devblack/image/upload/users/";
		StringBuffer sBuffer = new StringBuffer();
		
		sBuffer.append(URL).append(id).append(".jpg");
		URL url = new URL(sBuffer.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();            
		connection.addRequestProperty("Request-Method","GET");      
		connection.setDoInput(true);    
		connection.setDoOutput(false);    
		connection.connect();   
		String retorno = "https://res.cloudinary.com/devblack/image/upload/users/sem-foto.jpg";

		if(connection.getResponseCode() == 200) {
			retorno = sBuffer.toString();
		}  
		
		this.urlImage = retorno;
		
		return urlImage;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
}