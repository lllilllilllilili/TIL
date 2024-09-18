package com.example.test.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .version("v1.0.0") //버전
            .title("Open API") //이름
            .description("test"); //설명
        return new OpenAPI()
            .info(info);
    }

}
