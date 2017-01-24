package me.star2478.springcloudStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.netflix.discovery.shared.Application;

import me.star2478.springcloudStrategy.dao.KnowledgeStrategyConfigDAO;
import me.star2478.springcloudStrategy.dto.KnowledgeStrategyConfigDTO;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StrategyMain.class)
@WebAppConfiguration
public class StrategyTest {

//	@Autowired
//	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
//	@Autowired
//	private UserRepository userRepository;
//	@Before
//	public void setUp() {
//		userRepository.deleteAll();
//	}
	
	@Autowired
	private RedisTemplate<String, String> testRedisTemplate;

	@Test
	public void test() throws Exception {
		 assertTrue( true );
//		// 创建三个User，并验证User总数
//		userRepository.save(new User(1L, "didi", 30));
//		userRepository.save(new User(2L, "mama", 40));
//		userRepository.save(new User(3L, "kaka11", 50));
//		Assert.assertEquals(3, userRepository.findAll().size());
//		// 删除一个User，再验证User总数
//		User u = userRepository.findOne(1L);
//		userRepository.delete(u);
//		Assert.assertEquals(2, userRepository.findAll().size());
//		// 删除一个User，再验证User总数
//		u = userRepository.findByUsername("mama");
//		userRepository.delete(u);
//		Assert.assertEquals(1, userRepository.findAll().size());
//		
//
////		KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO = new KnowledgeStrategyConfigDTO();
////		knowledgeStrategyConfigDTO.setKey("cmsAnalytics");
////		knowledgeStrategyConfigDTO.setDescription("cms");
////		knowledgeStrategyConfigDTO.setStatus(1);
////		knowledgeStrategyConfigDTO.setExpire(2000);
////		knowledgeStrategyConfigDTO.setOpTime("2018-01-01 00:00:00");
////		knowledgeStrategyConfigDAO.save(knowledgeStrategyConfigDTO);
	}
	
	@Test
	public void testRedis() {
		// 保存字符串
		testRedisTemplate.opsForValue().set("aaa", "111");
		Assert.assertEquals("111", testRedisTemplate.opsForValue().get("aaa"));

	}

}
