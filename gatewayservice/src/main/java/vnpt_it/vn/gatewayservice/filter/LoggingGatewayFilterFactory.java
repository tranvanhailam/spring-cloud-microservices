package vnpt_it.vn.gatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("Logging")
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {
    private final Logger logger = LoggerFactory.getLogger(LoggingGatewayFilterFactory.class);

    public LoggingGatewayFilterFactory() {
        super(Config.class);
    }

//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            logger.info("Pre GatewayFilter logging");
//            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
//            return chain.filter(exchange)
//                    .then(Mono.fromRunnable(() -> {
//                        logger.info("Post GatewayFilter logging");
//                    }));
//        };
//    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            logger.info("Pre GatewayFilter logging");
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        logger.info("Post GatewayFilter logging");
                    }));
        }, -1);
    }

    public static class Config {
        private String baseMsg;
        public String getBaseMsg() {
            return baseMsg;
        }
        public void setBaseMsg(String baseMsg) {
            this.baseMsg = baseMsg;
        }
    }
}
