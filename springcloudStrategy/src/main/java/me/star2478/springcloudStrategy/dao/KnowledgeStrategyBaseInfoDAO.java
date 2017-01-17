package me.star2478.springcloudStrategy.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import me.star2478.springcloudStrategy.dto.KnowledgeStrategyBaseInfoDTO;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO;

public interface KnowledgeStrategyBaseInfoDAO extends MongoRepository<KnowledgeStrategyBaseInfoDTO, Long> {
	KnowledgeStrategyBaseInfoDTO getBaseInfoByKey(String key);

	public void upsertBaseInfo(String key, String appId, String os, String appVersion);
	
//	public KnowledgeStrategyBaseInfoDTO getBaseInfo(String key);
//	
//	public List<KnowledgeStrategyBaseInfoDTO> getAllBaseInfo();
//	
//	public void batchUpsertBaseInfo(String key, List<String> appId, List<String> os, List<String> appVersion);
//	
//	public void dropCol(String name);
}