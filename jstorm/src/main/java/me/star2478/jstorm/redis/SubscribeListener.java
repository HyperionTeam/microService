package me.star2478.jstorm.redis;

import org.springframework.util.StringUtils;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.tuple.Values;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class SubscribeListener extends JedisPubSub {

	private SpoutOutputCollector collector;

//	private String redis_uv_congestion_key = PropertiesUtil.getProperty("redis_uv_congestion_key");
//	private String redis_crash_congestion_key = PropertiesUtil.getProperty("redis_crash_congestion_key");
//	private String redis_uv_key = PropertiesUtil.getProperty("redis_uv_key");
//	private String redis_crash_key = PropertiesUtil.getProperty("redis_crash_key");

	
	/**
	 * 拥塞控制 消费后把redis里面的crash或uv数量减1
	 * @param channel
	 */
//	private void consumeCongestion(String channel) {
//
//		if (StringUtils.isEmpty(channel)) {
//			return;
//		}
//		try {
//			JedisPool jedisPool = (JedisPool) TopologyMain.ctx.getBean("jedisPool");
//			Jedis jedis = jedisPool.getResource();
//			String numberStr = null;
//			String key = null;
//			if (redis_uv_key.equals(channel)) {
//				numberStr = jedis.get(redis_uv_congestion_key);
//				key = redis_uv_congestion_key;
//			} else if (redis_crash_key.equals(channel)) {
//				numberStr = jedis.get(redis_crash_congestion_key);
//				key = redis_crash_congestion_key;
//			}
//
//			if (!StringUtils.isEmpty(numberStr)) {
//				int number = Integer.parseInt(numberStr);
//				number--;
//				jedis.set(key, number + "");
//			}
//
//			jedisPool.returnResource(jedis);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	public SubscribeListener(SpoutOutputCollector collector) {
		this.collector = collector;

	}

	@Override
	public void onMessage(String channel, String message) {
		// TODO Auto-generated method stub
//		consumeCongestion(channel);
		if (!StringUtils.isEmpty(message)) {
			collector.emit(new Values(message));
		}
	}

	@Override
	public void onPMessage(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPSubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPUnsubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnsubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
