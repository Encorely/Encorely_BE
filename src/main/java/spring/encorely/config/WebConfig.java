package spring.encorely.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:8080",
                        "http://13.209.39.26:8080",
                        "http://3.34.105.231:8080")
                .allowedMethods("*")
                .allowCredentials(true);
    }

}
