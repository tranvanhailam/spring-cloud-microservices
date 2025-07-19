package vnpt_it.vn.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotificationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationserviceApplication.class, args);
	}

}
