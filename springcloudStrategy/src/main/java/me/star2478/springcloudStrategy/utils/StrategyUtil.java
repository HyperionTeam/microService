package me.star2478.springcloudStrategy.utils;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;

import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO;


public class StrategyUtil {

	public final static int STATUS_DISABLE = 0; // 策略禁用
	public final static int STATUS_ENABLE = 1; // 策略启用
	
	public final static int TIME_EQUAL = 0; // 时间相等
	public final static int TIME_GREATER = 1; // 第一个时间比第二个时间大
	public final static int TIME_LESS = 2; // 第一个时间比第二个时间小
	
	public final static String INTERVAL_KNOWLEDGE_REDIS_FIELD = ":"; // 知识数据在redis field的间隔符
	
	private final static Logger logger = Logger.getLogger(StrategyUtil.class);
	
	/**
	 * 计算过期时间
	 * 
	 * @return
	 */
	public static String calExpireTime(int expire) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, expire);
		return sdf.format(cal.getTime());
	}

	/**
	 * 对比时间
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int compareTime(String time1, String time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			int c = (int) (date1.getTime() - date2.getTime());
			if (c > 0) {
				return TIME_GREATER;
			} else if (c < 0) {
				return TIME_LESS;
			} else {
				return TIME_EQUAL;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TIME_EQUAL;
		}
	}
	
	/**
	 * 判断time是否已过期，time早于当前时间则过期
	 * @return true：过期，false：未过期
	 */
	public static boolean checkTimeOut(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = sdf.format(new Date());
		 // 已过期
		if (StrategyUtil.compareTime(curTime, time) == TIME_GREATER) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检查策略配置是否合法
	 * @param knowledgeStrategyConfig
	 * @return
	 */
	public static boolean checkStrategyConfig(KnowledgeStrategyConfigDTO knowledgeStrategyConfig) {
		if (knowledgeStrategyConfig == null) {
			logger.error("config not exists");
			return false;
		}
		// 未配置任何知识条件
		if(knowledgeStrategyConfig.getAppStrategyTriggers() == null || knowledgeStrategyConfig.getAppStrategyTriggers().size() <= 0) {
			logger.warn("has no any appStrategyTrigger");
			return false;
		}
		// 检查策略状态
		if (knowledgeStrategyConfig.getStatus() == STATUS_DISABLE) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 拼接
	 * @param appId
	 * @param os
	 * @param appVersion
	 * @param triggerName
	 * @return
	 */
	public static String strcatKnowledgeRedisField(String appId, String os, String appVersion, String triggerName) {
		return String.format("%s"+ INTERVAL_KNOWLEDGE_REDIS_FIELD +"%s"+ INTERVAL_KNOWLEDGE_REDIS_FIELD +"%s"+ INTERVAL_KNOWLEDGE_REDIS_FIELD +"%s", 
				appId, os, appVersion, triggerName);
	}
	
	/**
	 * 解析知识数据在redis中的field
	 * @param strField
	 * @return
	 */
	public static Map<String, Object> splitKnowledgeRedisField(String strField) {
		Map<String, Object> fieldData = new HashMap<String, Object>();
		String[] arrField = strField.split(INTERVAL_KNOWLEDGE_REDIS_FIELD);
		fieldData.put("appId", arrField[0]);
		fieldData.put("os", arrField[1]);
		fieldData.put("appVersion", arrField[2]);
		fieldData.put("triggerName", arrField[3]);
		return fieldData;
	}
	
	/**
	 * 判断是否是个数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		if(str == null) {
			return false;
		}
		String reg = "^[0-9]+(\\.[0-9]+)?$";
		return str.matches(reg);
  }
}
