package me.star2478.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaMain {
	public static void main(String[] args) {
		SpringApplication.run(EurekaMain.class, args);
//		new SpringApplicationBuilder(EurekaMain.class).web(true).run(args);
	}
}
