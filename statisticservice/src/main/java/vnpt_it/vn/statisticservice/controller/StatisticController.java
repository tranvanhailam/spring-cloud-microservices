package vnpt_it.vn.statisticservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vnpt_it.vn.statisticservice.model.StatisticDTO;
import vnpt_it.vn.statisticservice.service.StatisticService;

import java.util.List;

@RestController
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping("/statistic")
    public StatisticDTO createStatistic(@RequestBody StatisticDTO statisticDTO) throws InterruptedException {
//        throw new RuntimeException("Not Implemented");
        this.statisticService.addStatistic(statisticDTO);
        return statisticDTO;
    }

    @GetMapping("/statistics")
    public List<StatisticDTO> getStatistics() {
        return statisticService.getStatistics();
    }
}
