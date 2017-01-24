package me.star2478.springcloudGoverA.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document(collection = "knowledgeStrategyConfig")
@Component("KnowledgeStrategyConfigDTO")
@CompoundIndexes({
    @CompoundIndex(background=true,unique=true,name ="knowledge_strategy_key_uidx", def ="{'key': -1}")
})
public class KnowledgeStrategyConfigDTO {

//	@Id
    private String _id;
	
	private String key;	//策略名，全局唯一
	private String description;	//策略说明
	private int status;	//策略状态：0表示禁用，1表示启用
	private int expire;	//知识过期时间
	private String opTime;	//最近操作时间
	private List<AppStrategyTrigger> appStrategyTriggers = new ArrayList<AppStrategyTrigger>();	//应用触发配置
	



	public String get_id() {
		return _id;
	}



	public void set_id(String _id) {
		this._id = _id;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public int getExpire() {
		return expire;
	}



	public void setExpire(int expire) {
		this.expire = expire;
	}



	public String getOpTime() {
		return opTime;
	}



	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}



	public List<AppStrategyTrigger> getAppStrategyTriggers() {
		return appStrategyTriggers;
	}



	public void setAppStrategyTriggers(List<AppStrategyTrigger> appStrategyTriggers) {
		this.appStrategyTriggers = appStrategyTriggers;
	}

//
//
//	public int getPersistent() {
//		return persistent;
//	}
//
//
//
//	public void setPersistent(int persistent) {
//		this.persistent = persistent;
//	}



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public static class AppStrategyTrigger{
		private String name;	// trigger名
		private String sql;	// sql语句
		private String op;	// 用于判断是否触发应用的操作符，包括<、>、=、>=、<=
		private Object value;	// 用于判断是否触发应用的value阀值
		private String appStrategyName;	// 应用策略名
		private int persistent;	//是否持久化：0表示不持久化，1表示持久化
		public String getOp() {
			return op;
		}
		public void setOp(String op) {
			this.op = op;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public String getAppStrategyName() {
			return appStrategyName;
		}
		public void setAppStrategyName(String appStrategyName) {
			this.appStrategyName = appStrategyName;
		}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getPersistent() {
			return persistent;
		}
		public void setPersistent(int persistent) {
			this.persistent = persistent;
		}
		
	}

}
