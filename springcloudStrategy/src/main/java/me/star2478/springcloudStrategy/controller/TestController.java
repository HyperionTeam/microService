package me.star2478.springcloudStrategy.controller;


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

import me.star2478.springcloudStrategy.dao.KnowledgeStrategyConfigDAO;
import me.star2478.springcloudStrategy.dao.KnowledgeStrategyStatDAO;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO;
import me.star2478.springcloudStrategy.redis.User;
//import me.star2478.springcloudStrategy.redis.User;

@RestController
public class TestController {
    private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private KnowledgeStrategyStatDAO knowledgeStrategyStatDAO;
	
	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
	
    @Autowired
    private RestTemplate restTemplate;

    // redis
	@Autowired
	private RedisTemplate<String, String> redisTemplateString;
	
	@Autowired
	private StringRedisTemplate redisTemplateHash;
	
    @Autowired
	private RedisTemplate<String, User> redisTemplateObject;


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
    
    /**
     * redis普通set和get
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/redisSetString")
    public String redisSetString(String key, String value) {
    	ValueOperations<String, String> ops = redisTemplateString.opsForValue();
        if (!redisTemplateString.hasKey(key)) {  
            ops.set(key, "fodo");  
            logger.warn("Found key " + key + ", value=" + ops.get(key) );  
        } else {  
            logger.warn("key is " + key + " exists!");  
        }  
    	
    	ops.set(key, value);
    	return ops.get(key);
    }
    
    /**
     * redis普通hash的set、get、del、expire
     * @param key
     * @param field
     * @param value
     * @return
     */
    @RequestMapping(value = "/redisHSetString")
    public boolean redisHSetString(String key, String field, Float value) {
    	HashOperations<String, Object, Object> ops = redisTemplateHash.opsForHash();
    	ops.put(key, field, value.toString());
    	ops.increment(key, field, 3.1);
        ops.put(key, field + "-1", ((Float)(value + 1l)).toString());
        ops.put(key, field + "-2", ((Float)(value + 1l)).toString());
    	if (!redisTemplateHash.hasKey(key)) {
            logger.warn("key not exist");
        } else {
        	logger.warn("key is exist");
        }
    	// 获取所有field
    	printAllFieldsAndValues(ops, key);
        logger.warn("==========");
    	ops.delete(key, field + "-2");
    	printAllFieldsAndValues(ops, key);
    	// 设置过期时间
//        redisTemplateHash.expire(key, 10, TimeUnit.SECONDS);
    	return true;
    }
    
    /**
     * redis对非string value的set/get
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/redisSetObject")
    public Integer redisSetObject(String key, Integer value) {
    	User user = new User(key, value);
    	ValueOperations<String, User> ops = redisTemplateObject.opsForValue();
		ops.set(user.getUsername(), user);
		return redisTemplateObject.opsForValue().get(key).getAge().intValue();
    }
    
    // 打印redis hash的所有field和value
    private void printAllFieldsAndValues(HashOperations<String, Object, Object> ops, String key) {

        Set<Object> fields = ops.keys(key);
        for (Object echoField : fields) {
            logger.warn(echoField+": "+ops.get(key, echoField));
        }
    }
}
