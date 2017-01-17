package me.star2478.springcloudStrategy.controller;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import me.star2478.springcloudStrategy.dao.KnowledgeStrategyConfigDAO;
import me.star2478.springcloudStrategy.dao.KnowledgeStrategyStatDAO;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO;

@RestController
public class TestController {
    private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private KnowledgeStrategyStatDAO knowledgeStrategyStatDAO;
	
	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
	
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "testFallback")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(@RequestParam int type) {
    	String result = "";
    	if(type == 0) {
    		result = restTemplate.getForEntity("http://GOVERB/goverB?a=10&b=20", String.class).getBody();
    	} else if (type == 1) {
    		result = restTemplate.getForEntity("http://GOVERA/goverA?a=11&b=21", String.class).getBody();
		} else if (type == 2) {
			try {
	    		TimeUnit.MILLISECONDS.sleep(2000);
	    	} catch(Exception e) {	// note：就算此处被捕获也会触发fallback
	    		e.printStackTrace();
	    	}
			result = "sleep";
		}
    	return result;
    }
    
    // fallback，参数要和call一致
    public String testFallback(int type) {
    	logger.error("这里被fallback了," + type);
        return "error";
    }
    
    @RequestMapping(value = "/getStatData")
    public HashMap<String, Object> getStatData() {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	result.put("count", knowledgeStrategyStatDAO.count());
    	result.put("data", knowledgeStrategyStatDAO.findAll());
    	return result;
    }
    
    @RequestMapping(value = "/getConfigByName")
    public KnowledgeStrategyConfigDTO getConfigByName(String key) {
//    	HashMap<String, Object> result = new HashMap<String, Object>();
    	KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO = knowledgeStrategyConfigDAO.getConfigByKey(key);
    	KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO1 = knowledgeStrategyConfigDAO.getConfigByName(key);
    	System.out.println("-----" + knowledgeStrategyConfigDTO.get_id());
    	System.out.println("-----" + knowledgeStrategyConfigDTO1.get_id());
    	return knowledgeStrategyConfigDTO;
    }
}
