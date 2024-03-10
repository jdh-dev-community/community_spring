package com.jdh.community_spring.utils;


import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import javax.servlet.*;

public abstract class TestInitializer {
  @Container
  public static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
          .withExposedPorts(6379);

  protected InMemoryDBProvider initRedisProvider() {
    String host = redis.getHost();
    Integer port = redis.getFirstMappedPort();
    LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
    factory.afterPropertiesSet();

    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(factory);
    template.afterPropertiesSet();
    return new InMemoryDBProvider(template);
  }

  protected MockMvc initMockMvc(WebApplicationContext ctx, Filter filter, String filterPattern) {
    return MockMvcBuilders
            .webAppContextSetup(ctx)
            .addFilter(filter, filterPattern)
            .build();
  }
}
