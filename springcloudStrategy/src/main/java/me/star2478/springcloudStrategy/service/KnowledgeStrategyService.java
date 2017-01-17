package me.star2478.springcloudStrategy.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import me.star2478.springcloudStrategy.dao.KnowledgeStrategyBaseInfoDAO;
import me.star2478.springcloudStrategy.dao.KnowledgeStrategyConfigDAO;
import me.star2478.springcloudStrategy.dao.KnowledgeStrategyStatDAO;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO.AppStrategyTrigger;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyStatDTO;
import me.star2478.springcloudStrategy.utils.StrategyUtil;


/**
 * 
 * @author heliuxing
 *
 */
//@Component
@Service
public class KnowledgeStrategyService { 

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
	
	@Autowired
	private KnowledgeStrategyStatDAO knowledgeStrategyStatDAO;
	
	@Autowired
	private KnowledgeStrategyBaseInfoDAO knowledgeStrategyBaseInfoDAO;

    @Autowired
    RestTemplate restTemplate;
    
	/**
	 * 接收数据流
	 * 取出策略配置，解析sql知识条件
	 * 如果数据流满足sql知识条件，则产生知识数据，过期时间>0则将知识存入redis，=0则不存入redis
	 * 如果知识数据满足应用条件，启动对应的应用策略或持久化
	 * 
	 * @param key 知识策略名
	 * @param appId app唯一标示
	 * @param os 操作系统
	 * @param appVersion app版本号
	 * @param data 知识数据field和value
	 * @return
	 */
	public boolean call(String key, String appId, String os, String appVersion, Map<String, Object> data) {
		
		data.put("appId", appId);
		data.put("os", os);
		data.put("appVersion", appVersion);
		
		// 获取知识策略的配置
		KnowledgeStrategyConfigDTO knowledgeStrategyConfig = getKnowledgeStrategyConfig(key);
		if (StrategyUtil.checkStrategyConfig(knowledgeStrategyConfig) == false) {
			return false;
		}
		int expire = knowledgeStrategyConfig.getExpire();
		List<AppStrategyTrigger> appStrategyTriggers = knowledgeStrategyConfig.getAppStrategyTriggers();
		
		/////// 调用下游的治理服务
		test(key, appId, os, appVersion, data);
		///////
		
		// 将基本信息同步到mongodb
		synBaseInfo2Mongodb(key, appId, os, appVersion);
		
//		// 读出redis中的指定key的所有field和value
//		KnowledgeRedis<String> knowledgeRedisForStringBean = factory.getKnowledgeRedisBeanDefault();
//		Map<byte[], byte[]> knowledgeInRedis = knowledgeRedisForStringBean.hgetAll(key.getBytes());
//		
//		// 扫描所有知识条件和应用条件
//		for (AppStrategyTrigger appStrategyTrigger : appStrategyTriggers) {
//			int persistent = appStrategyTrigger.getPersistent();
//			String triggerName = appStrategyTrigger.getName();
//			String sql = appStrategyTrigger.getSql();
//			
//			// 分析数据流，检查是否符合知识条件，如果符合则产生知识数据
//			Map<String, Object> newKnowledgeMap = SQLEngine.createKnowledge(sql, data);
//			// 如果数据流不满足知识条件，则检查下一个知识条件
//			if(newKnowledgeMap == null) {
//				continue;
//			}
//			String command = (String) newKnowledgeMap.get(SQLEngine.COMMAND);
//			String keyOfKnowledge = (String) newKnowledgeMap.get(SQLEngine.KEYOFKNOWLEDGE);
//			Object knowledgeVal = newKnowledgeMap.get(SQLEngine.VALUE);
//			if(knowledgeVal == null) {
//				continue;
//			}
//			KnowledgeStrategy knowledgeStrategy = factory.getKnowledgeStrategyBeanByCommand(command);
//			
//			// 如果redis中当前知识条件对应的知识数据已过期且要求持久化，则把知识数据持久化到mongodb
//			byte[] expireTimeByte = knowledgeInRedis.get(KnowledgeStrategy.EXPIRETIME_KEYNAME.getBytes());
//			String fieldForRedis = StrategyUtil.strcatKnowledgeRedisField(appId, os, appVersion, triggerName);
//			if (persistent == 1 && expireTimeByte != null && StrategyUtil.checkTimeOut(new String(expireTimeByte))) {
//				logger.info("key=" + key + " expireTimeout, persistent triggerName=" + triggerName + " into mongodb");
//				Map<String, Object> persistentMap = knowledgeStrategy.getPersistentKnowledgeByField(knowledgeInRedis, fieldForRedis);
//				saveData2MogonDB(key, persistentMap, command);
//			}
//			
//			// 将知识数据inc或set进redis，得到新知识数据
//			Object[] arrValue = knowledgeStrategy.getCurrentValue(key, appId, os, appVersion, triggerName, knowledgeVal, knowledgeInRedis, expire);
//			Object curVal = arrValue[KnowledgeStrategy.INDEX_CURRENT_VALUE];
//			Object persistentedVal = arrValue[KnowledgeStrategy.INDEX_PERSISTENTED_VALUE];
//			
//			// 判断应用条件是否符合
//			String op4AppStrategy = appStrategyTrigger.getOp();
//			Object value4AppStrategy = appStrategyTrigger.getValue();
//			if (!knowledgeStrategy.checkOpenAppStrategy(curVal, op4AppStrategy, value4AppStrategy)) {
//				continue;
//			}
//			// 启动应用策略
//			String appStrategyName = appStrategyTrigger.getAppStrategyName();
//			logger.info("knowledge_strategy[" + key + "] hit app_strategy[" + appStrategyName + "]");
//			if (appStrategyService.call(appStrategyName, curVal, data)) {	// 执行成功应用策略
//				// 将数据持久化到mongodb
//				if (persistent == 1) {
//					Object val2Persistent = knowledgeStrategy.calValue2Persistent(curVal, persistentedVal);
//					Map<String, Object> persistentMap = new HashMap<String, Object>();
//					persistentMap.put(fieldForRedis, val2Persistent);
//					saveData2MogonDB(key, persistentMap, command);
//				}
//				// 删除redis对应的知识数据，从零开始统计知识数据
//				knowledgeStrategy.hdelKnowledgeInRedis(key.getBytes(), fieldForRedis.getBytes(), (fieldForRedis + KnowledgeStrategy.PERSISTENTED_SUFFIX).getBytes());
//			} else {
//				logger.error("app_strategy[" + appStrategyName + "] open fail");
//				return false;
//			}	
//		}
		return true;
	}
	
	/**
	 * 保存数据到mongodb
	 * @param key 对应知识策略的策略名
	 * @param persistentMap map的key为field,value为要保存的数据
	 * @param command：表示对mongodb采用inc还是set
	 */
//	public void saveData2MogonDB(String key, Map<String, Object> persistentMap, String command) {
//		// 获取当前时间的半点或者整点
//		String nowTime = CommonUtil.sdf.format(new Date());
//		String adjustNowtime = CommonUtil.getAdjustTime(nowTime);
//
//		for (String field : persistentMap.keySet()) {
//			Object value = persistentMap.get(field);
//			Map<String, Object> fieldData = StrategyUtil.splitKnowledgeRedisField(field);
//			KnowledgeStrategyStatDTO newKnowledgeStrategyStat = new KnowledgeStrategyStatDTO();
//			newKnowledgeStrategyStat.setKey(key);
//			newKnowledgeStrategyStat.setAppId(fieldData.get("appId").toString());
//			newKnowledgeStrategyStat.setOs(fieldData.get("os").toString());
//			newKnowledgeStrategyStat.setAppVersion(fieldData.get("appVersion").toString());
//			newKnowledgeStrategyStat.setTriggerName(fieldData.get("triggerName").toString());
//			newKnowledgeStrategyStat.setTime(adjustNowtime);
//			
//			switch (command) {
//			case SQLEngine.INC:
//				knowledgeStrategyStatDAO.upsertIncKnowledgeStrategyStat(newKnowledgeStrategyStat, value);
//				break;
//	
//			case SQLEngine.SET:
//				knowledgeStrategyStatDAO.upsertSetKnowledgeStrategyStat(newKnowledgeStrategyStat, value);
//				break;
//			}
//		}
//	}
	
	/**
	 * 将app基本信息进行同步：如果在redis中key没有对应的appId、os、appVersion，则将这些app基本信息存入redis和mongodb
	 * @param key
	 * @param appId
	 * @param os
	 * @param appVersion
	 */
	private void synBaseInfo2Mongodb(String key, String appId, String os, String appVersion) {
//		byte keyByte[] = (key + KnowledgeStrategy.REDIS_BASE_INFO_SUFFIX).getBytes();
//		KnowledgeRedis<String> knowledgeRedisForStringBean = factory.getKnowledgeRedisBeanDefault();
//		Map<byte[], byte[]> redisData = knowledgeRedisForStringBean.hgetAll(keyByte);
//		Map<byte[], byte[]> mapByte = new HashMap<byte[], byte[]>();
//		
//		// 解析基本信息，将要存入redis和mongodb的信息项存入mapByte
//		for (KnowledgeStrategy.BaseInfoEnum baseInfoItem : KnowledgeStrategy.BaseInfoEnum.values()) {
//			String baseInfoKey = baseInfoItem.name();
//			byte baseInfoValueBtye[] = redisData.get(baseInfoKey.getBytes());
//			// 如果redis里不存在某项基本信息field
//			if (baseInfoValueBtye == null) {
//				if (baseInfoKey.equals(KnowledgeStrategy.BaseInfoEnum.APPID.toString())) {
//					mapByte.put(baseInfoKey.getBytes(), appId.getBytes());
//				} else if (baseInfoKey.equals(KnowledgeStrategy.BaseInfoEnum.OS.toString())) {
//					mapByte.put(baseInfoKey.getBytes(), os.getBytes());
//				} else if (baseInfoKey.equals(KnowledgeStrategy.BaseInfoEnum.APPVERSION.toString())) {
//					mapByte.put(baseInfoKey.getBytes(), appVersion.getBytes());
//				}
//			} else {	// 如果redis已存在某项基本信息field，就要判断是否包含相应的value
//				String baseInfoValue = new String(baseInfoValueBtye);
//				List<String> baseInfoList = Arrays.asList(baseInfoValue.split(KnowledgeStrategy.REDIS_BASE_INFO_SEPARATOR));
//				if (baseInfoKey.equals(KnowledgeStrategy.BaseInfoEnum.APPID.toString())) {
//					if (!baseInfoList.contains(appId)) {
//						mapByte.put(baseInfoKey.getBytes(), (baseInfoValue + KnowledgeStrategy.REDIS_BASE_INFO_SEPARATOR + appId).getBytes());
//					}
//				} else if (baseInfoKey.equals(KnowledgeStrategy.BaseInfoEnum.OS.toString())) {
//					if (!baseInfoList.contains(os)) {
//						mapByte.put(baseInfoKey.getBytes(), (baseInfoValue + KnowledgeStrategy.REDIS_BASE_INFO_SEPARATOR + os).getBytes());
//					}
//				} else if (baseInfoKey.equals(KnowledgeStrategy.BaseInfoEnum.APPVERSION.toString())) {
//					if (!baseInfoList.contains(appVersion)) {
//						mapByte.put(baseInfoKey.getBytes(), (baseInfoValue + KnowledgeStrategy.REDIS_BASE_INFO_SEPARATOR + appVersion).getBytes());
//					}
//				}
//			}
//		}
//		
//		// 需要将app基本信息存入redis和mongodb
//		if(mapByte.size() > 0) {

			// 存mongodb
			knowledgeStrategyBaseInfoDAO.upsertBaseInfo(key, appId, os, appVersion);
			
//			// 存redis
//			knowledgeRedisForStringBean.hmset(keyByte, mapByte);
//		}
	}

	/**
	 * 获取知识策略配置
	 * 
	 * @param name
	 * @return
	 */
	private KnowledgeStrategyConfigDTO getKnowledgeStrategyConfig(String key) {

		KnowledgeStrategyConfigDTO knowledgeStrategyConfig = null;
		// 先从local内存取///////////////

		// local内存没有，则从mongodb取
		knowledgeStrategyConfig = knowledgeStrategyConfigDAO.getConfigByKey(key);

		// 写进local内存///////////////
		return knowledgeStrategyConfig;
	}
	
	private void test(String key, String appId, String os, String appVersion, Map<String, Object> data) {

		int screenSize = Integer.valueOf(data.get("screenSize").toString());
		if (screenSize <= 10) {
			Object result = restTemplate.getForEntity("http://GOVERA/hello", String.class).getBody();
			System.out.println("\n\nresult:" + result);
		} else {

			Object result = restTemplate.getForEntity("http://GOVERB/hello", String.class).getBody();
			System.out.println("\n\nresult:" + result);
		}
//		KnowledgeStrategyStatDTO knowledgeStrategyStatDTO = new KnowledgeStrategyStatDTO();
//		knowledgeStrategyStatDTO.setAppId(appId);
//		knowledgeStrategyStatDTO.setAppVersion(appVersion);
//		knowledgeStrategyStatDTO.setKey(key);
//		knowledgeStrategyStatDTO.setOs(os);
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String curTime = df.format(new Date());
//		knowledgeStrategyStatDTO.setTime(curTime);
//		knowledgeStrategyStatDTO.setTriggerName("test");
//		knowledgeStrategyStatDTO.setValue(screenSize);
//		knowledgeStrategyStatDAO.save(knowledgeStrategyStatDTO);
	}
	
}
