package woodspring.springnice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import woodspring.springnice.service.GitRepositoryService;

@RestController
@RequestMapping(value="/git")
public class GitRepoController {
	private final static Logger logger = LoggerFactory.getLogger(GitRepoController.class);

	@Autowired
	GitRepositoryService gitRepoImpl;
	
	@GetMapping("/repo/{item}")
	public String getGitRepository(@PathVariable(required = true) String item) {
		String result = gitRepoImpl.reqGitRepos(item);
		logger.info("item:{}", item);
		return result;
		
	}
	

}
