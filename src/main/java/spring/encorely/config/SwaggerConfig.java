package spring.encorely.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Encorely API").version("1.0.0")
                .description("Encorely API 명세서")
                .version("1.0.0");

        String jwtSchemeName = "JWT TOKEN";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));


        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080"))  // 로컬 개발 환경
                .addServersItem(new Server().url("http://3.34.105.231:8080"))  // 운영 환경
                .addServersItem(new Server().url("http://13.209.39.26:8080")) // 개발 환경
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}