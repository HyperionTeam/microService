package me.star2478.springcloudGoverC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GoverCMain {
	public static void main(String[] args) {
		SpringApplication.run(GoverCMain.class, args);
//		new SpringApplicationBuilder(GoverCMain.class).web(true).run(args);
	}
}
