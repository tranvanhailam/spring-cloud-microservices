package vnpt_it.vn.statisticservice.service;


import vnpt_it.vn.statisticservice.domain.Statistic;
import vnpt_it.vn.statisticservice.model.StatisticDTO;

import java.util.List;

public interface StatisticService {
    void addStatistic(StatisticDTO statisticDTO);
    List<StatisticDTO> getStatistics();
}
