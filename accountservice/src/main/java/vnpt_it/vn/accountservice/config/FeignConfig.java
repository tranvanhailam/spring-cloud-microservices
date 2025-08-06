package vnpt_it.vn.accountservice.config;

import feign.RequestInterceptor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(Tracer tracer, Propagator propagator) {
        return requestTemplate -> {
            // Lấy span hiện tại
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                // Thêm các header tracing vào request
                propagator.inject(currentSpan.context(), requestTemplate, (template, key, value) ->
                        template.header(key, value));
            }
        };
    }
}