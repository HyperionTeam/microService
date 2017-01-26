package me.star2478.springcloudGoverA.controller;


import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class RibbonController {
    private final Logger logger = Logger.getLogger(getClass());
	
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 访问其他微服务
     * @param type
     * @return
     */
    @HystrixCommand(fallbackMethod = "ribbonFallback")
    @RequestMapping(value = "/ribbon", method = RequestMethod.GET)
    public String ribbon(@RequestParam int type) {
    	String result = "";
    	if(type == 0) {
    		result = restTemplate.getForEntity("http://GOVERB/goverB?a=10&b=20", String.class).getBody();
    	} else if (type == 1) {
    		result = restTemplate.getForEntity("http://GOVERC/goverC?a=11&b=21", String.class).getBody();
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
    public String ribbonFallback(int type) {
    	logger.error("这里被fallback了," + type);
        return "error";
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(@RequestParam int type) {
    	RestTemplate restTemplateTest = new RestTemplate();
    	String result = restTemplateTest.getForEntity("http://localhost:4101/goverB?a=10&b=20", String.class).getBody();
    	return result;
    }
}
