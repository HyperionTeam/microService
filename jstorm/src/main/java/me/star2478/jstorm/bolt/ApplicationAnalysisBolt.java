package me.star2478.jstorm.bolt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.mongodb.DB;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import me.star2478.jstorm.JstormMain;
import me.star2478.jstorm.common.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import storm.trident.testing.IFeeder;

public class ApplicationAnalysisBolt extends BaseRichBolt {

	private OutputCollector collector;

	private String redis_strategy_key = PropertiesUtil.getProperty("redis_strategy_key");

	protected MongoTemplate mongoTemplate;

	private static Logger logger = Logger.getLogger(ApplicationAnalysisBolt.class);

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		mongoTemplate = JstormMain.ctx.getBean(MongoTemplate.class);
	}

	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		try {
			Object data = input.getValueByField(redis_strategy_key);
			logger.info("============application bolt working");
			logger.info("============knowledge from knowledge-bolt: " + data.toString());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Crash Analysis Exception:" + e.toString());
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
	}

}
