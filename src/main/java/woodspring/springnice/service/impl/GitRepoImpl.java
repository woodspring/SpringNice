package woodspring.springnice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import woodspring.springnice.entity.GitRepositoryLink;
import woodspring.springnice.repository.GitRepository;
import woodspring.springnice.service.GitRepositoryService;

@Service
public class GitRepoImpl implements GitRepositoryService {
	private final static Logger logger = LoggerFactory.getLogger(GitRepoImpl.class);
	ConcurrentHashMap<String, ArrayList<GitRepositoryLink>> gitMap = new ConcurrentHashMap<>();
	@Autowired
	GitRepository gitStore;

	public ArrayList<GitRepositoryLink> requestGithub(String reqStr, int page) {
		ArrayList<GitRepositoryLink> retList = new ArrayList();
		String baseUrl = "https://github.com/search?q";
		String theUrl = String.format("%s=%s&type=Repository&p=%d", baseUrl, reqStr, page);
		logger.info("theUrl:{}", theUrl);
		theUrl = theUrl + page;
		RestTemplate restTemplate = new RestTemplate();
//Send request with GET method and default Headers.
		String result = restTemplate.getForObject(theUrl, String.class);
		retList = getRepo(result, reqStr);
		logger.info("Result:{}", retList.size());


		return retList;
	}

	private ArrayList<GitRepositoryLink> getRepo(String result, String org) {
		ArrayList<GitRepositoryLink> retList = new ArrayList<>();
		Scanner scanner = new Scanner(result);
		int ind = 0;
		boolean isName = false;
		String theName = "";
		GitRepositoryLink gitNode = new GitRepositoryLink();
		gitNode.setOrganization(org);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (isName) {
				theName = line;
				isName = false;
				gitNode.setBody(theName);
			}
			if (line.contains("global_relay_id")) {
				int pos = line.indexOf("https:");
				// logger.info("WHAT get:{}", line.subSequence(pos, pos + 100));
				int lastPos = line.indexOf("}", pos);
				String theStr = line.substring(pos, lastPos - 6);
				gitNode.setHtml_url(theStr);
				String[] strList = theStr.split("\\/");
				String bodyStr = String.format("%s/%s", strList[3], strList[4]);
				gitNode.setName(bodyStr);
				bodyStr = String.format("%d        %s/%s", strList.length, strList[3], strList[4]);

				if ( gitStore != null)
				gitNode = gitStore.save(gitNode);
				retList.add(gitNode);
				logger.info("gitNode:{}", gitNode.toString());
				gitNode = new GitRepositoryLink();
				gitNode.setOrganization(org);

			}
			if (line.contains("mb-1")) {
				isName = true;
				// next line is the name
			}
		}
		scanner.close();
		return retList;
	}

	@Override
	public String reqGitRepos(String reqStr) {
		// gitList = new ArrayList<>();
		// ArrayList<GitRepositoryLink> retList = new ArrayList<GitRepositoryLink>();
		logger.info("reqStr:{}, gitMap:{} find:{}", reqStr, gitMap.size(), gitMap.size(), gitMap.containsKey(reqStr));
		StringBuffer strBuf = new StringBuffer();
		if (gitMap.containsKey(reqStr)) {
			logger.info("reqStr:{} has existed in gitMap", reqStr);
			try {
				List<GitRepositoryLink> gitNodeList = new ArrayList<GitRepositoryLink>();
				ObjectMapper mapper = new ObjectMapper();
				List<String> strList = gitStore.findByOrganization(reqStr).stream().map(node -> {
					String jsonString = new String();
					try {
						jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;
					return jsonString;
				}).collect(Collectors.toList());

				for (String item : strList) {
					strBuf.append(item);
				}

				logger.info("strBuf:{}", strBuf.length());
				return strBuf.toString();
			} catch (Exception ex) {
				new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			ArrayList<GitRepositoryLink> retList = new ArrayList<>();
			for (int ind = 0; ind < 5; ind++) {
				retList.addAll(requestGithub(reqStr, ind));
				// requestGithub(reqStr, ind);
			}

			gitMap.put(reqStr, retList);

			retList = gitMap.get(reqStr);
			logger.info("reqStr:{} -> RetList:{}", reqStr, retList.size());
			ObjectMapper mapper = new ObjectMapper();

			strBuf.append("organization : " + reqStr + ",");
			for (GitRepositoryLink item : retList) {
				String jsonString = new String();
				try {
					jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				strBuf.append(jsonString);
			}
			logger.info("RetList:{}", retList.size());
		}
		return strBuf.toString();
	}

}
