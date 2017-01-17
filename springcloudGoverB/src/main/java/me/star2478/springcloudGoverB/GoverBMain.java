package me.star2478.springcloudGoverB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//import zipkin.server.EnableZipkinServer;

//@EnableZipkinServer
@EnableDiscoveryClient
@SpringBootApplication
public class GoverBMain {
	public static void main(String[] args) {
		SpringApplication.run(GoverBMain.class, args);
//		new SpringApplicationBuilder(GoverBMain.class).web(true).run(args);
	}
}
