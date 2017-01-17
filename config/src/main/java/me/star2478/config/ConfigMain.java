package me.star2478.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableDiscoveryClient	// 服务注册到Eureka
@EnableConfigServer  // 启动config server，采集配置，其他服务可以使用，这里改了，所有依赖服务都可以实时感知
@SpringBootApplication
public class ConfigMain {
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigMain.class, args);
	}
}
