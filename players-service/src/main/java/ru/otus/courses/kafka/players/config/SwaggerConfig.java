package ru.otus.courses.kafka.players.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@OpenAPIDefinition(servers = {@Server(url = "${swagger.server.url:/}", description = "Default Server URL")})
public class SwaggerConfig implements WebMvcConfigurer {

  @Bean
  public OpenAPI api() {
    return new OpenAPI()
        .info(new Info()
            .title("Players service")
            .description("Service that implements player profiles management")
            .version("1.0-SNAPSHOT"));
  }
}
