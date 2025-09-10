package vnpt_it.vn.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import vnpt_it.vn.gatewayservice.filter.LoggingGatewayFilterFactory;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service-router",
                        r -> r.path("/userinfo/**")
//                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://auth-service"))
                .route("account-service-router",
                        r -> r.path("/accounts/**")
                                .uri("lb://account-service"))
                .route("company-service-router",
                        r -> r.path("/companies/**")
                                .uri("lb://company-service"))
                .route("job-service-router",
                        r -> r.path("/jobs/**")
                                .uri("lb://job-service"))
                .route("resume-service-router",
                        r -> r.path("/resumes/**")
                                .uri("lb://resume-service"))
                .route("skill-service-router",
                        r -> r.path("/skills/**")
                                .uri("lb://skill-service"))
                .route("subscriber-service-router",
                        r -> r.path("/subscribers/**")
                                .uri("lb://subscriber-service"))
                // Swagger OpenAPI routes
                .route("swagger-account-service",
                        r -> r.path("/v3/api-docs/account-service")
                                .filters(f -> f.setPath("/v3/api-docs"))
                                .uri("lb://account-service"))
                .route("swagger-statistic-service",
                        r -> r.path("/v3/api-docs/statistic-service")
                                .filters(f -> f.setPath("/v3/api-docs"))
                                .uri("lb://statistic-service"))
                .route("swagger-notification-service",
                        r -> r.path("/v3/api-docs/notification-service")
                                .filters(f -> f.setPath("/v3/api-docs"))
                                .uri("lb://notification-service"))
                .build();
    }
}
