package me.star2478.springcloudGoverA.redis;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

/**
 * 
 * ！！！！！！！！！！！！！！！！！！！！！！！！！！！
 * 高能高能高能：如果value是String，不需要此配置类！！
 * 高能高能高能：如果value不是String对象，比如User，需要此配置类，且需要自己实现对象的序列化和反序列化，见RedisObjectSerializer
 * 
 * redis配置类，注入必要的bean
 * @author heliuxing
 *
 */
@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private Integer port;

	@Value("${spring.redis.password}")
	private String password;
	
	// 此处配置了host、port、pwd，需要改进，研究一下如何不需要这里的配置
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
    	JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
    	jedisConnectionFactory.setHostName(host);
    	jedisConnectionFactory.setPort(port);
    	jedisConnectionFactory.setPassword(password);
    	return jedisConnectionFactory;
    }

    // springboot已经自动将JedisConnectionFactory注入进来了
    @Bean
//    public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory factory) {
    public RedisTemplate<String, User> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, User> template = new RedisTemplate<String, User>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());	// key的序列化/反序列化用spring自带的
        template.setValueSerializer(new RedisObjectSerializer());	// value的序列化/反序列化用我们自己定义的
        return template;
    }

    //spring boot自动加载redis配置及注入org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.java 中
    //这边已经注入了JedisConnectionFactory，并从配置中读取配置
//    @Bean
//    @ConditionalOnMissingBean({RedisConnectionFactory.class})
//    public JedisConnectionFactory redisConnectionFactory() throws UnknownHostException {
//    	return this.applyProperties(this.createJedisConnectionFactory());
//    }
//
//    private JedisConnectionFactory createJedisConnectionFactory() {
//    	return this.properties.getPool() != null?new JedisConnectionFactory(this.getSentinelConfig(), this.jedisPoolConfig()):new JedisConnectionFactory(this.getSentinelConfig());
//    }
}
