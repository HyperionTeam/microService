package me.star2478.hystrixDashboard;

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
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableTurbine
@EnableHystrixDashboard	// Hystrix dashbord
@EnableDiscoveryClient	// 服务注册到Eureka
@SpringBootApplication
public class HystrixDashboardMain {
	
	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardMain.class, args);
//		new SpringApplicationBuilder(StrategyMain.class).web(true).run(args);
	}
}
