package com.example.hyuntrace.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>(); //선언
        template.setConnectionFactory(factory); //Redis 연결 정보를 넣어주는 과정
        template.setKeySerializer(new StringRedisSerializer()); //키를 문자열로 지정
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); //값 또한 지정(json)

        return template;
    }
}
