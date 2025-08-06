package vnpt_it.vn.statisticservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vnpt_it.vn.statisticservice.domain.Statistic;
import vnpt_it.vn.statisticservice.model.StatisticDTO;
import vnpt_it.vn.statisticservice.repository.StatisticRepository;
import vnpt_it.vn.statisticservice.util.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(StatisticServiceImpl.class);

    public StatisticServiceImpl(StatisticRepository statisticRepository, ModelMapper modelMapper) {
        this.statisticRepository = statisticRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addStatistic(StatisticDTO statisticDTO) {
        logger.info(">>>>>>>>>> StatisticService StatisticServiceImpl: addStatistic");
        Statistic statistic = this.modelMapper.mapStatisticDTOToStatistic(statisticDTO);
        this.statisticRepository.save(statistic);
    }

    @Override
    public List<StatisticDTO> getStatistics() {
        List<Statistic> statistics = this.statisticRepository.findAll();
        List<StatisticDTO> statisticDTOs = statistics.stream()
                .map(statistic -> this.modelMapper.mapStatisticToStatisticDTO(statistic))
                .collect(Collectors.toList());
        return statisticDTOs;
    }
}
