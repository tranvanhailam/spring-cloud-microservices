package vnpt_it.vn.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountserviceApplication.class, args);
	}

}
