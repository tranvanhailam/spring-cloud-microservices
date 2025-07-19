package vnpt_it.vn.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vnpt_it.vn.accountservice.model.StatisticDTO;

@FeignClient(name = "statistic-service", url = "http://localhost:8082")
public interface StatisticService {
    @PostMapping("/statistic")
    public void createStatistic(@RequestBody StatisticDTO statisticDTO);
}
