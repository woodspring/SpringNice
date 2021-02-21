package woodspring.springnice.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import woodspring.springnice.entity.GitRepositoryLink;

public interface GitRepository extends JpaRepository<GitRepositoryLink, Long>{
	
	ArrayList<GitRepositoryLink> findByOrganization(String organization);

}