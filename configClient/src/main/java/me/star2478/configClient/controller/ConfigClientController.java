package me.star2478.configClient.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


// 获取配置中心的配置
@RefreshScope	// 可以支持refresh
@RestController
public class ConfigClientController {
//    private final Logger logger = Logger.getLogger(getClass());
    
    @Value("${ddd}")
    private String abc;
    
    @RequestMapping("/abc")
    public String abc() {
//    	System.out.println("-----------hahahhaha");
        return this.abc;
    }

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

}
