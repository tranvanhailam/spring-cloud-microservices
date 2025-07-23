package vnpt_it.vn.accountservice;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class AccountserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountserviceApplication.class, args);
	}

//	@Bean
//	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> connectorCustomizer() {
//		return factory -> {
//			// Cổng mặc định (được cấu hình trong application.properties, ví dụ: 8080)
//			// Thêm cổng bổ sung
//			Connector additionalConnector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//			additionalConnector.setPort(8080); // Cổng thứ hai
//			factory.addAdditionalTomcatConnectors(additionalConnector);
//		};
//	}

}
