package com.prannoy.usermanagementservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${swagger.info.title}")
    private  String title;

    @Value("${swagger.info.email}")
    private  String email;

    @Value("${swagger.info.name}")
    private  String name;

    @Value("${swagger.info.version}")
    private  String version;

    @Value("${swagger.info.description}")
    private  String description;

    @Value("${swagger.info.url}")
    private  String url;

    private final Info info = new Info();

    private void setInfo(){
        final Contact contact = new Contact()
                .name(name)
                .email(email);
        this.info
                .title(title)
                .description(description)
                .version(version)
                .contact(contact);
    }

    @Bean
    public OpenAPI openAPIConfig() {
        this.setInfo();
        return new OpenAPI()
                .info(this.info);
    }
}
