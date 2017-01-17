package me.star2478.springcloudStrategy.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document(collection = "knowledgeStrategyBaseInfo")
@Component("KnowledgeStrategyBaseInfoDTO")
@CompoundIndexes({
    @CompoundIndex(background=true,unique=true,name ="knowledge_strategy_baseinfo_key_uidx", def ="{'key': -1}")
})
public class KnowledgeStrategyBaseInfoDTO {
	
//	@Id
    private String _id;
	
	private String key;	//策略名，全局唯一
	private String[] APPID;	//app唯一标示
	private String[] OS;	//操作系统
	private String[] APPVERSION;	//app版本号
	
	
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
	public String[] getAPPID() {
		return APPID;
	}
	public void setAPPID(String[] aPPID) {
		APPID = aPPID;
	}
	public String[] getOS() {
		return OS;
	}
	public void setOS(String[] oS) {
		OS = oS;
	}
	public String[] getAPPVERSION() {
		return APPVERSION;
	}
	public void setAPPVERSION(String[] aPPVERSION) {
		APPVERSION = aPPVERSION;
	}

	
}
