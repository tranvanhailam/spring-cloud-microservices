package vnpt_it.vn.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vnpt_it.vn.gatewayservice.filter.LoggingGatewayFilterFactory;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder, LoggingGatewayFilterFactory loggingGatewayFilterFactory) {
        return builder.routes()
                .route("account-service-router",
                        r -> r.path("/user/**")
                                .filters(f -> f.stripPrefix(1)
                                        .filter(loggingGatewayFilterFactory.apply(new LoggingGatewayFilterFactory.Config())))
//                                        .circuitBreaker(c -> c.setName("CircuitBreaker")
//                                                .getFallbackUri()))
                                .uri("lb://account-service"))
                .route("statistic-service-router",
                        r -> r.path("/report/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://statistic-service"))
                .route("notification-service-router",
                        r -> r.path("/notification/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://notification-service"))
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
