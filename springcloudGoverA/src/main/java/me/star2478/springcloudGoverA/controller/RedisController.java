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

import me.star2478.springcloudGoverA.redis.User;

@RestController
public class RedisController {
    private final Logger logger = Logger.getLogger(getClass());
    
    // redis
	@Autowired
	private RedisTemplate<String, String> redisTemplateString;
	
	@Autowired
	private StringRedisTemplate redisTemplateHash;
	
    @Autowired
	private RedisTemplate<String, User> redisTemplateObject;

    
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
