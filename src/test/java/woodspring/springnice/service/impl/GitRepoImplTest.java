package woodspring.springnice.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import woodspring.springnice.entity.GitRepositoryLink;

class GitRepoImplTest {
private final static Logger logger = LoggerFactory.getLogger(GitRepoImplTest.class);
	@Test
	void testGitRepoImpl() {
		GitRepoImpl gitImpl = new GitRepoImpl();
		String retStr = gitImpl.reqGitRepos("apache");
		logger.info("test GitRepoimpl size:{}, result:{}",retStr.length(),  retStr );
		assertNotNull( retStr);
	}

}
