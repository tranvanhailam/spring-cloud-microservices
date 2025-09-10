package vnpt_it.vn.gatewayservice.auth;

import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {
    private final WebClient webClient;

    public AuthServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9000").build();
    }

    public Mono<ResUserInfo> getUserLogin(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        return webClient.get()
                .uri("/userinfo")
                .headers(headers -> {
                    if (authHeader != null) {
                        headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                    }
                })
                .retrieve()
                .bodyToMono(ResUserInfo.class); // không block nữa
    }
}
