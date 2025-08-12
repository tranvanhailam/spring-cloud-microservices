//package vnpt_it.vn.accountservice.statistic;
//
//
//import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class StatisticServiceFallback implements StatisticService {
//    Logger logger = LoggerFactory.getLogger(StatisticServiceFallback.class);
//
//    @CircuitBreaker(name = "statistic-service", fallbackMethod = "fallbackCircuitBreakerOPEN")
//    @Override
//    public StatisticDTO createStatistic(StatisticDTO statisticDTO) {
//        logger.error("Error connect or exception from Statistic Service");
//        throw new RuntimeException("Error connect or exception from Statistic Service");
////        return null;
//    }
//
//    public void fallbackCircuitBreakerOPEN(CallNotPermittedException ex) {
//        // Xử lý khi CircuitBreaker OPEN
//        logger.error("CallNotPermittedException");
//        throw ex;
//    }
//}
