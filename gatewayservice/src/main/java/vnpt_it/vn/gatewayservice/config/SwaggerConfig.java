package vnpt_it.vn.gatewayservice.config;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfigProperties) {
        // Enable Swagger UI
        swaggerUiConfigProperties.setEnabled(true);
        // Configure multiple Swagger URLs
        swaggerUiConfigProperties.setUrl("/v3/api-docs/account-service");
        swaggerUiConfigProperties.setUrl("/v3/api-docs/statistic-service");
        swaggerUiConfigProperties.setUrl("/v3/api-docs/notification-service");
        return new SwaggerUiConfigParameters(swaggerUiConfigProperties);
    }
}
