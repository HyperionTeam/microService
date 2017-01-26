package me.star2478.springcloudGoverB.controller;


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

//@RefreshScope	// 可以支持refresh
@RestController
public class GoverBController {
    private final Logger logger = Logger.getLogger(getClass());

//    @Value("${ddd}")
//    private String abc;
    
    @Autowired
    private DiscoveryClient client;
    
    @RequestMapping(value = "/goverB", method = RequestMethod.GET)
    public Integer goverB(@RequestParam Integer a, @RequestParam Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        Integer r = a + b;
//        logger.info("====" + abc);
        logger.info("/goverB, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
        return r;
    }
    
    @RequestMapping(value = "/hello" ,method = RequestMethod.GET)
    public String hello() {
//        ServiceInstance instance = client.getLocalServiceInstance();
//        logger.info("/hello, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());
        return "GoverB: Hello World";
    }
}
