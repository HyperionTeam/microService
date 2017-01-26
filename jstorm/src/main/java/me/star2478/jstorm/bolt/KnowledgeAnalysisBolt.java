package me.star2478.jstorm.bolt;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import me.star2478.jstorm.common.PropertiesUtil;

public class KnowledgeAnalysisBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = 1L;

	private OutputCollector collector;

	private String redis_strategy_key = PropertiesUtil.getProperty("redis_strategy_key");
	
	private static Logger logger = Logger.getLogger(KnowledgeAnalysisBolt.class);


	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;

	}

	public void execute(Tuple input) {
		
		try {
			String strategyJson = input.getStringByField(redis_strategy_key);
			logger.info("============strategyJson from redis queue:" + strategyJson);
			logger.info("============sql parsing");
			logger.info("============knowledge parsing");
			JSONObject strategyObject = JSON.parseObject(strategyJson);
			String knowledge = strategyObject.get("appId").toString();
			collector.emit(new Values(knowledge));
		} catch (Exception e) {
			logger.error("strategy spout parse Exception:" + e);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields(redis_strategy_key));
		
	}
	
	

}
