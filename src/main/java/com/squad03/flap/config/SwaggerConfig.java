package com.squad03.flap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FLAP - Sistema Kanban API")
                        .description("API REST para sistema de gerenciamento de tarefas estilo Kanban. " +
                                   "Esta API permite criar, atualizar, mover e deletar tarefas em um quadro Kanban.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Squad 03")
                                .email("squad03@flap.com")
                                .url("https://github.com/squad03"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor de Desenvolvimento"));
    }
}