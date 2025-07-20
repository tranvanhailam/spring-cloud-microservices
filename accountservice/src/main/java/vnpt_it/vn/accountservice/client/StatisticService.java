package vnpt_it.vn.accountservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vnpt_it.vn.accountservice.model.StatisticDTO;

@FeignClient(name = "statistic-service", url = "http://localhost:8082", fallback = StatisticServiceFallback.class)
public interface StatisticService {
    @PostMapping("/statistic")
    public StatisticDTO createStatistic(@RequestBody StatisticDTO statisticDTO);
}
