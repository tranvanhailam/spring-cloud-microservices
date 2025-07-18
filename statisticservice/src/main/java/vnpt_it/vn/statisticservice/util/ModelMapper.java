package vnpt_it.vn.statisticservice.util;

import org.springframework.stereotype.Component;
import vnpt_it.vn.statisticservice.domain.Statistic;
import vnpt_it.vn.statisticservice.model.StatisticDTO;

@Component
public class ModelMapper {
    public Statistic mapStatisticDTOToStatistic(StatisticDTO statisticDTO) {
        Statistic statistic = new Statistic();
        statistic.setId(statisticDTO.getId());
        statistic.setMessage(statisticDTO.getMessage());
//        statistic.setCreatedDate(statisticDTO.getCreatedDate());
        return statistic;
    }

    public StatisticDTO mapStatisticToStatisticDTO(Statistic statistic) {
        StatisticDTO statisticDTO = new StatisticDTO();
        statisticDTO.setId(statistic.getId());
        statisticDTO.setMessage(statistic.getMessage());
//        statisticDTO.setCreatedDate(statistic.getCreatedDate());
        return statisticDTO;
    }
}
