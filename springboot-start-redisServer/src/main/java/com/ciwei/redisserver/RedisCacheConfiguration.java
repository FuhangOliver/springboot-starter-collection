package com.ciwei.redisserver;

import com.ciwei.redisserver.mybatiscache.ApplicationContextHolder;
import com.ciwei.redisserver.redisimpl.RedisHandle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class RedisCacheConfiguration {

    @Bean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }

    @Bean
    public RedisCommand redisCommand(){
        return new RedisHandle();
    }
}
