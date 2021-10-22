package me.shamanov.oms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("me.shamanov.oms.rest"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag("Customer Controller", "Controller to manipulate customers"),
                      new Tag("Order Controller", "Controller to manipulate orders"),
                      new Tag("Product Controller", "Controller to manipulate products"));
    }
}