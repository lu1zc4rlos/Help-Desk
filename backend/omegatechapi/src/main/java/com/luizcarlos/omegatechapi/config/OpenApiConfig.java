package com.luizcarlos.omegatechapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("OmegaTech API")
                        .version("1.0")
                        .description("API de gerenciamento de chamados técnicos")
                        .contact(new Contact()
                                .name("Luiz Carlos")
                                .email("jujuni1000.carlos@gmail.com")));

    }
}
