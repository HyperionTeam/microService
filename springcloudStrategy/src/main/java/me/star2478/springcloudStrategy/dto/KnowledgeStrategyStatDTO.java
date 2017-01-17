package me.star2478.springcloudStrategy.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

/**
 * 
 * @author heliuxing
 * 保存统计数据的表
 */
@Document(collection = "knowledgeStrategyStat")
@Component("KnowledgeStrategyStat")
@CompoundIndexes({
	@CompoundIndex(background=true,unique=true,name ="knowledge_strategy_stat_joinIdx", def ="{'key': -1, 'triggerName':-1, 'time':-1, 'appId':-1, 'os':-1, 'appVersion':-1}")
})
public class KnowledgeStrategyStatDTO {

//	@Id
    private String _id;
	private String key;    //对应知识策略的策略名
	private String triggerName;  //对应AppStrategyTrigger的name，比如说模块名
	private Object value;    //redis存储的数据
	private String time;   //数据的保存时间
	private String appId;	//app唯一标示
	private String os;	//操作系统
	private String appVersion;	//app版本号
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
}
