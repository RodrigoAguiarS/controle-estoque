package br.com.rodrigo.api.controleestoque.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API para Controle de Estoque")
                        .version("1.0")
                        .description("Esta API oferece suporte para controle de estoque."));
    }
}
