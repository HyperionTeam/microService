package me.star2478.springcloudStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker 	// Hystrix熔断器
@EnableHystrixDashboard	// Hystrix dashbord
@EnableDiscoveryClient	// 服务注册到Eureka
//@EnableTask				// 任务
@SpringBootApplication
public class StrategyMain {
	
	// 这是个配置类，在这里注册bean，可以被strategyController所用
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
//	@Bean
//    public CommandLineRunner commandLineRunner() {
//        return strings ->
//                System.out.println("--------Executed at :" + 
//                      new SimpleDateFormat().format(new Date()));
//    }
	
	public static void main(String[] args) {
		SpringApplication.run(StrategyMain.class, args);
//		new SpringApplicationBuilder(StrategyMain.class).web(true).run(args);
	}
}
