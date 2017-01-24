package me.star2478.springcloudGoverA.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import me.star2478.springcloudGoverA.dto.KnowledgeStrategyConfigDTO;

public interface KnowledgeStrategyConfigDAO extends MongoRepository<KnowledgeStrategyConfigDTO, Long> {
//public interface KnowledgeStrategyConfigDAO {

	KnowledgeStrategyConfigDTO getConfigByKey(String key);
//	public KnowledgeStrategyConfigDTO getConfigByKey(String key);
	
	public KnowledgeStrategyConfigDTO getConfigByName(String name);
	
//	public void insertKnowledgeStrategy(KnowledgeStrategyConfigDTO KnowledgeStrategyConfigDTO);
//	
//	public List<KnowledgeStrategyConfigDTO> getKnowledgeStrategyByPage(int skip ,int limit);
//	
//	public boolean deleteKnowledgeStrategyByName(String name);
//	
//	public boolean deleteKnowledgeStrategyByOpTime(String opTime);
//	
//	public boolean modifyKnowledgeStrategyByName(KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO);
//	
//	public long getKnowledgeStrategyNumber();
//	
//	public List<KnowledgeStrategyConfigDTO> getAllPersistentedStrategy(int status, int persistent, int expire);
	
//	public void dropCol();
}