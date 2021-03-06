# 微服务使用

> 各工程下src/main/resources/*.properties 纪录着本服务连接其他服务的ip，确保网络可通

### 介绍
* eureka：服务注册与发现中心
* config：分布式配置中心，可从github或本地文件提取配置
* configClient：配置客户端服务，访问配置中心中的配置
* springcloudGoverA：服务A，访问mongodb、redis、治理服务B/C，接入了hystrix容错、actuator监控和zipkin服务跟踪
* springcloudGoverB：服务B，接入了actuator监控和zipkin服务跟踪
* springcloudGoverC：服务C，接入了actuator监控和zipkin服务跟踪
* hystrix-dashboard：hystrix控制平台，可以查看有哪些接口存活、访问量、是否打开熔断器、异常率、超时率、响应时间分布等
* zipkin：Twitter开源的服务跟踪平台，可查看有哪些服务、哪些接口、服务间的依赖关系、响应时间等

![微服务框架图](https://github.com/HyperionTeam/microService/blob/master/%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%9B%BE.png)


### 使用

* 依次启动eureka、config、configClient、springcloudGoverA、springcloudGoverB、springcloudGoverC、hystrixDashboard
	* 启动方法：cd进各个服务目录，依次执行mvn install、java -jar target/xxx.jar
* 使用hystrix-dashboard，两种方法：
	* 方法1：hystrix-dashboard独立为一个微服务
		* 假设hystrixDashboard端口为8300，打开http://ip:8300/hystrix，把要监控的服务填入对话框（比如http://localhost:4200/hystrix.stream），点击monitor streams
	* 方法2：hystrix-dashboard放在tomcat里运行
		* [下载hystrix-dashboard-#.#.#.war](http://search.maven.org/#browse%7C1045347652)
		* 将war包放入tomcat启动：假设端口是8300，则通过http://ip:8300/hystrix-dashboard-#.#.#/index.html，把要监控的多个服务填入对话框，点击add stream -> monitor streams
* 使用zipkin
	* [下载zipkin jar](https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec)
	* 启动zipkin：java -jar zipkin-server-xx.jar，占用9411端口

### todo
* 接入jstorm
* 接入消息总线
* 接入doc
* 接入api网关
