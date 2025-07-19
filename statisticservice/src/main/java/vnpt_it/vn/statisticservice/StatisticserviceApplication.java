package vnpt_it.vn.statisticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StatisticserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisticserviceApplication.class, args);
	}

}
