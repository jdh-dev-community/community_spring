package com.jdh.community_spring.common.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class InMemoryDBProvider {

  private final StringRedisTemplate stringRedisTemplate;

  public void setTemperarily(String key, String value, long second) {
    stringRedisTemplate.opsForValue().set(key, value, Duration.ofMillis(second));
  }
  public void set(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  public String get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

}
