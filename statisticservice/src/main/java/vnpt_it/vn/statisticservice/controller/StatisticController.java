package vnpt_it.vn.statisticservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final Logger logger = LoggerFactory.getLogger(StatisticController.class);

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_log')")
    @PostMapping("/statistic")
    public StatisticDTO createStatistic(@RequestBody StatisticDTO statisticDTO) throws InterruptedException {
        logger.info(">>>>>>>>>> StatisticService StatisticController: createStatistic");
//        throw new RuntimeException("Not Implemented");
        this.statisticService.addStatistic(statisticDTO);
        return statisticDTO;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_log')")
    @GetMapping("/statistics")
    public List<StatisticDTO> getStatistics() {
        return statisticService.getStatistics();
    }
}
