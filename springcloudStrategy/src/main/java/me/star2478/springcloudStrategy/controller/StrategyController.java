package me.star2478.springcloudStrategy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import me.star2478.springcloudStrategy.dao.KnowledgeStrategyStatDAO;
import me.star2478.springcloudStrategy.service.KnowledgeStrategyService;

/**
 * 
 * @author heliuxing
 *
 */
@RestController
//@RequestMapping("/fsg/strategy")
public class StrategyController {
	
	private static final int MAX_NUM_TO_RECORD_RESULT = 10;	// 超过此值则不需要纪录结果，用于批量接口
	private static final int MAX_NUM_TO_HANDLE = 1000;	// 超过此值则不处理数据了，用于批量接口
	
	@Autowired
	private KnowledgeStrategyService commonKnowledgeStrategy;
	
	private final Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    RestTemplate restTemplate;

    
    // fallback，参数要和call一致
    public String callFallback(String info) {
    	logger.error("这里被fallback了," + info);
        return "fallback";
    }
	
	/**
	 * 采集统计数据web层
	 * @param data 数据详情
	 * @param key 数据类型，即dataType
	 * @param appId app唯一标示
	 * @param os 操作系统
	 * @param osVersion 操作系统版本号
	 * @param appVersion app版本号
	 * @param deviceId 设备号
	 * @param deviceName 设备名
	 * @param channel 渠道号
	 * @param location 位置
	 * @param ip ip号码
	 * @param mobilePhone 手机号
	 * @param networkEnv 网络环境类型
	 * @param telecomOperator 基础运营商名称
	 * @param time 数据发生时间，后台可根据数据类型的不同，选择性信任此参数
	 * @param userAgent 用户代理类型UA
	 * @param screenSize 屏幕大小
	 * @return
	 * 示例：http://localhost:4200/call?info={%22screenSize%22:10,%22appId%22:10017,%22os%22:%22android%22,%22appVersion%22:%221.0.1%22,%22data%22:%22{\%22server\%22:\%22GOVERB\%22,\%22api\%22:\%22hello\%22}%22,%22dataType%22:%22cmsAnalytics%22,%22channel%22:%22pingan%22}
	 * @throws Exception 
	 */

    @HystrixCommand(fallbackMethod = "callFallback")
    @RequestMapping(value = "/call", method = RequestMethod.GET)
    public String call(@RequestParam String info) throws Exception {
		
		try {
			Map<String, Object> dataMap = json2Map(info);
	    
	    	String data = dataMap.get("data").toString();
	    	Map<String, Object> mergeMap = json2Map(data);
	    	String dataType = dataMap.get("dataType").toString();
	    	String appId = dataMap.get("appId").toString();
	    	String os = dataMap.get("os").toString();
			String appVersion = dataMap.get("appVersion").toString();
			if (StringUtils.isBlank(dataType) || StringUtils.isBlank(data) || StringUtils.isBlank(appId)
					|| StringUtils.isBlank(os)
					|| StringUtils.isBlank(appVersion)) {
				logger.error("params fail! dataType=" + dataType + " data=" + data + " appId=" + appId +
						" os=" + os + " appVersion=" + appVersion);

				return "fail: invalid params";
			}
			mergeData(dataMap, mergeMap);
            boolean result = commonKnowledgeStrategy.call(dataType, appId, os, appVersion, dataMap);
            
			if (result) {
				return "success";
			} else {
				return "fail";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("to fallback");
		}
//		return "success";
	}

	/**
	 * json串转为map
	 * 
	 * @param jsonString
	 * @return
	 */
	private HashMap<String, Object> json2Map(String jsonString) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		// 遍历jsonObject数据，添加到Map对象
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	
	/**
	 * json串转为array
	 * 
	 * @param jsonString
	 * @return
	 */
	private static List<HashMap<String, String>> json2Array(String jsonString) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// 将json数组字符串转换成jsonArray
		JSONArray jsonArray	= JSONArray.parseArray(jsonString);
		
		// 遍历jsonArray
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			HashMap<String, String> map = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				String value = (entry.getValue() != null)?entry.getValue().toString():null;
				map.put(entry.getKey(), value);
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 将dataMap和mergeMap合并到dataMap，如果dataMap中包含了和mergeMap一样的key，
	 * 以mergeMap中的value为准
	 * 
	 * @param dataMap
	 * @param mergeMap
	 */
	private void mergeData(Map<String, Object> dataMap, Map<String, Object> mergeMap) {
		for (Map.Entry<String, Object> entry : mergeMap.entrySet()) {
			if(entry.getValue() != null) {
				dataMap.put(entry.getKey(), entry.getValue());
			}
		}
	}

}
