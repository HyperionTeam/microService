package me.star2478.springcloudGoverA.controller;


import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import me.star2478.springcloudGoverA.dao.KnowledgeStrategyConfigDAO;
import me.star2478.springcloudGoverA.dto.KnowledgeStrategyConfigDTO;
import me.star2478.springcloudGoverA.redis.User;

@RestController
public class MongoDBController {
    private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;

    
    @RequestMapping(value = "/getConfigByName")
    public KnowledgeStrategyConfigDTO getConfigByName(String key) {
//    	HashMap<String, Object> result = new HashMap<String, Object>();
    	KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO = knowledgeStrategyConfigDAO.getConfigByKey(key);
    	KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO1 = knowledgeStrategyConfigDAO.getConfigByName(key);
    	if(knowledgeStrategyConfigDTO != null) {
    		System.out.println("-----" + knowledgeStrategyConfigDTO.get_id());
    	}
    	if(knowledgeStrategyConfigDTO1 != null) {
    		System.out.println("-----" + knowledgeStrategyConfigDTO1.get_id());
    	}
    	return knowledgeStrategyConfigDTO;
    }
    
}
