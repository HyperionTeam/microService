package me.star2478.springcloudStrategy.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import me.star2478.springcloudStrategy.dto.KnowledgeStrategyBaseInfoDTO;


//@Component("KnowledgeStrategyBaseInfoDAO")
//public class KnowledgeStrategyBaseInfoDAOImpl extends BaseMongoDAO<KnowledgeStrategyBaseInfoDTO> implements KnowledgeStrategyBaseInfoDAO {
public class KnowledgeStrategyBaseInfoDAOImpl {
	private final static String KNOWLEDGE_STRATEGY_CONFIG_COL = "knowledgeStrategyBaseInfo";	//mongodb表名

	@Autowired
	protected MongoTemplate mongoTemplate;
	
//	@Override
	public void upsertBaseInfo(String key, String appId, String os, String appVersion) {
		if (appId == null && os == null && appVersion == null) {
			return;
		}
//		MongoTemplate mongoTemplate = getMongoTemplate();
		Update update = new Update();
		if (appId != null) {
//			update.addToSet(KnowledgeStrategy.BaseInfoEnum.APPID.toString(), appId);
			update.addToSet("APPID", appId);
		}
		if (os != null) {
//			update.addToSet(KnowledgeStrategy.BaseInfoEnum.OS.toString(), os);
			update.addToSet("OS", os);
		}
		if (appVersion != null) {
//			update.addToSet(KnowledgeStrategy.BaseInfoEnum.APPVERSION.toString(), appVersion);
			update.addToSet("APPVERSION", appVersion);
		}
		Query q = new Query(Criteria.where("key").is(key));
		mongoTemplate.upsert(q, update, KnowledgeStrategyBaseInfoDTO.class);
	}
	
//	@Override
//	public KnowledgeStrategyBaseInfoDTO getBaseInfo(String key) {
//		return this._get(Criteria.where("key").is(key));
//	}
//	
//	@Override
//	public List<KnowledgeStrategyBaseInfoDTO> getAllBaseInfo() {
//		Criteria criteria = new Criteria();
//		return this._list(criteria);
//	}
//	
//	@Override
//	public void batchUpsertBaseInfo(String key, List<String> appId, List<String> os, List<String> appVersion) {
//		if (appId == null && os == null && appVersion == null) {
//			return;
//		}
//		MongoTemplate mongoTemplate = getMongoTemplate();
//		Update update = new Update();
//		if (appId != null) {
//			for (String string : appId) {
//				update.set(KnowledgeStrategy.BaseInfoEnum.APPID.toString(), string);
//			}
//		}
//		if (os != null) {
//			for (String string : os) {
//				update.set(KnowledgeStrategy.BaseInfoEnum.OS.toString(), string);
//			}
//		}
//		if (appVersion != null) {
//			for (String string : appVersion) {
//				update.set(KnowledgeStrategy.BaseInfoEnum.APPVERSION.toString(), string);
//			}
//		}
//		Query q = new Query(Criteria.where("key").is(key));
//		mongoTemplate.upsert(q, update, KnowledgeStrategyBaseInfoDTO.class);
//	}
//	
//	@Override
//	public void dropCol(String name) {
//		MongoTemplate mongoTemplate = getMongoTemplate();
//		mongoTemplate.dropCollection(name);
//	}
}