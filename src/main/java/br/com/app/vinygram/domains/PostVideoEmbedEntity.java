package br.com.app.vinygram.domains;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

@Entity
@Table(name = "posts_video")
public class PostVideoEmbedEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_POST_VIDEO", nullable = false)
	private long id;
	@NotEmpty
	@NotNull
	@Column(name = "SITE", nullable = true)
	private String site;
	@NotEmpty
	@NotNull
	@Column(name = "CODVIDEOEMBED", nullable = false)
	private String codVideoEmbed;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(columnDefinition = "POST", referencedColumnName = "ID_POST")
	private PostEntity post;
	
	@Transient
	private String caminhoVideoString; 
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getCodVideoEmbed() {
		return codVideoEmbed;
	}
	public void setCodVideoEmbed(String codVideoEmbed) {
		this.codVideoEmbed = codVideoEmbed;
	}
	public PostEntity getPost() {
		return post;
	}
	public void setPost(PostEntity post) {
		this.post = post;
	}
	
	public String getCaminhoVideoString() {
		this.caminhoVideoString = "https://"+this.getSite()+"/embed/"+this.getCodVideoEmbed();
		return caminhoVideoString;
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
		PostVideoEmbedEntity other = (PostVideoEmbedEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}	
}