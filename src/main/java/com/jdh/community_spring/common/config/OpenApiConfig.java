package com.jdh.community_spring.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Value("${swagger.title}")
  private String title;

  @Value("${swagger.version}")
  private String version;

  @Value("${swagger.description}")
  private String description;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info().title(title)
                    .version(version)
                    .description(description));
  }
}