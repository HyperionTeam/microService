package me.star2478.springcloudGoverA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//import zipkin.server.EnableZipkinServer;

//@EnableZipkinServer
@EnableDiscoveryClient
@SpringBootApplication
public class GoverAMain {
	public static void main(String[] args) {
		SpringApplication.run(GoverAMain.class, args);
//		new SpringApplicationBuilder(GoverAMain.class).web(true).run(args);
	}
}
