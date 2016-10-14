package com.gnet.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
public class RedisTokenStoreConfiguration {

	@Bean
    @Autowired
    public TokenStore tokenStore(JedisConnectionFactory factory) {
//    	return new RedisTokenStore(factory);
		return new InMemoryTokenStore();
    }
	
}
