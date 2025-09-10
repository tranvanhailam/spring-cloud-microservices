package vnpt_it.vn.gatewayservice.filter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vnpt_it.vn.gatewayservice.auth.AuthServiceClient;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {
    private final Supplier<BucketConfiguration> bucketConfiguration;
    private final ProxyManager<String> proxyManager;
    private final AuthServiceClient authServiceClient;

    public RateLimitFilter(Supplier<BucketConfiguration> bucketConfiguration, ProxyManager<String> proxyManager,
                           AuthServiceClient authServiceClient) {
        this.bucketConfiguration = bucketConfiguration;
        this.proxyManager = proxyManager;
//        this.authService = authService;
        this.authServiceClient = authServiceClient;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return this.authServiceClient.getUserLogin(exchange)
                .flatMap(currentUserLogin -> {
                    System.out.println("Current user: " + currentUserLogin);

                    Bucket bucket = proxyManager.builder().build(currentUserLogin.getSub(), bucketConfiguration);
                    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

                    if (probe.isConsumed()) {
                        exchange.getResponse().getHeaders()
                                .add("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));

                        exchange.mutate().request(
                                exchange.getRequest().mutate().header("X-User", currentUserLogin.getSub()).build()
                        ).build();

                        return chain.filter(exchange);
                    } else {
                        long waitForRefillSeconds = probe.getNanosToWaitForRefill();
                        exchange.getResponse().getHeaders()
                                .add("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefillSeconds));
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                        byte[] msg = "You have exceeded the API request limit".getBytes(StandardCharsets.UTF_8);
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(msg);

                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    }
                });
    }

    @Override
    public int getOrder() {
        return -1; // Ưu tiên cao
    }
}