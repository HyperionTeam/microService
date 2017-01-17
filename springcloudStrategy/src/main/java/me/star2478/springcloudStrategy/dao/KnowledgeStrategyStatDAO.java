package me.star2478.springcloudStrategy.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyStatDTO;

public interface KnowledgeStrategyStatDAO extends MongoRepository<KnowledgeStrategyStatDTO, Long> {
	KnowledgeStrategyStatDTO getStatByKey(String key);
}