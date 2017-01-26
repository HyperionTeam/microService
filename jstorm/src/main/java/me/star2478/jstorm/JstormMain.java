package me.star2478.jstorm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//import org.springframework.beans.factory.annotation.Value;

//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.context.annotation.Bean;

import com.alibaba.jstorm.daemon.nimbus.TopologyAssign;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.TopologyInitialStatus;
import backtype.storm.topology.TopologyBuilder;
import me.star2478.jstorm.bolt.ApplicationAnalysisBolt;
import me.star2478.jstorm.bolt.KnowledgeAnalysisBolt;
import me.star2478.jstorm.common.PropertiesUtil;
import me.star2478.jstorm.spout.StrategySpout;

//@EnableDiscoveryClient	// 服务注册到Eureka
//@SpringBootApplication
public class JstormMain {
	
	private static final Logger logger = Logger.getLogger(JstormMain.class);

	public static LinkedHashMap<String, Object> fileInfoMap;
	public static ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");

	public static Set<String> appIps = new HashSet<String>();
	public static Set<String> appNames = new HashSet<String>();
	
	public static Map<String,String> IOS_MODULE_MAP;
	public static Map<String,String> ANDROID_MODULE_MAP;
	
	
	private static String jstorm_worker_num = PropertiesUtil.getProperty("jstorm_worker_num");
	private static String jstorm_acker_num = PropertiesUtil.getProperty("jstorm_acker_num");
	
	private static String nimbus_host = PropertiesUtil.getProperty("nimbus.host");
	
	private static String strategy_spout_num = PropertiesUtil.getProperty("strategy_spout_num");
	private static String knowledge_analysis_bolt_num = PropertiesUtil.getProperty("knowledge_analysis_bolt_num");
	private static String application_analysis_bolt_num = PropertiesUtil.getProperty("application_analysis_bolt_num");
	
	
	public static void main(String[] args) {
		topologyInit(args);
//		SpringApplication.run(JstormMain.class, args);
	}
	
	private static void topologyInit(String[] args) {
		try {
			
			String nimbusHost = nimbus_host;
			
			// configure & build topology
			TopologyBuilder builder = new TopologyBuilder();
			
			builder.setSpout("strategy-spout", new StrategySpout(),Integer.parseInt(strategy_spout_num));
			builder.setBolt("knowledge-analysis-bolt", new KnowledgeAnalysisBolt(), Integer.parseInt(knowledge_analysis_bolt_num)).shuffleGrouping("strategy-spout");
			builder.setBolt("application-analysis-bolt", new ApplicationAnalysisBolt(), Integer.parseInt(application_analysis_bolt_num)).shuffleGrouping("knowledge-analysis-bolt");
			
			// submit topology
			Config conf = new Config();
			String name = JstormMain.class.getSimpleName();
			if (args != null && args.length > 0) {
				String nimbusConf = args[0];
				conf.setNumAckers(Integer.parseInt(jstorm_acker_num));
				conf.put(nimbusHost, nimbusConf);
				conf.setNumWorkers(Integer.parseInt(jstorm_worker_num));
				try {
					StormSubmitter.submitTopologyWithProgressBar(name, conf,
							builder.createTopology());
				} catch (AlreadyAliveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidTopologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				conf.setMaxTaskParallelism(3);
				LocalCluster cluster = new LocalCluster();
				cluster.submitTopology(name, conf, builder.createTopology());
				Thread.sleep(10*60*60*1000);
				cluster.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

