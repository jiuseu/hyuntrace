package com.example.hyuntrace.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){

        return new OpenAPI().info(new Info()
                .title("SpringDoc UI")
                .description("SpringDoc를 이용한 UI 테스트")
                .version("0.0.1"))
                .components(new Components());

        //나중에 보안 기능 추가
    }


    public GroupedOpenApi BoardApi(){
        return GroupedOpenApi.builder()
                .group("Board Rest API")
                .build();
    }
}
