package woodspring.springnice.service;

import java.util.ArrayList;

import woodspring.springnice.entity.GitRepositoryLink;

public interface GitRepositoryService {
	
	String reqGitRepos(String reqStr);
	ArrayList<GitRepositoryLink> requestGithub(String reqStr, int page);

}
