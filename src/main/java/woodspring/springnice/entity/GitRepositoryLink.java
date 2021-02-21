package woodspring.springnice.entity;

import javax.persistence.*;


@Entity
@Table(name = "gitrepository")
public class GitRepositoryLink { //implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Column(name = "organization")
	String organization;
	
	@Column(name = "name")
	String name;
	
	@Column(name = "body")
	String body;
	
	@Column(name = "html_url")
	String html_url;
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getHtml_url() {
		return html_url;
	}
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	@Override
	public String toString() {
		return "repository: [id=" + id + ", name=" + name + ", body=" + body + ", html_url=" + html_url + "]";
	}
	

	

}