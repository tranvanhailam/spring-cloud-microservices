//package vnpt_it.vn.accountservice.notification;
//
//import feign.RequestInterceptor;
//import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//
//@Configuration
//public class NotificationFeignClientConfiguration {
//    private final String CLIENT_REGISTRATION_ID = "notification-service";
//    @Bean("NotificationFeignClientConfiguration")
//    public RequestInterceptor requestInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
//        //config gọi đến authorization server để lấy về token,
//        //tự động chèn vào RequestInterceptor của open feign gửi lên resource server (notification service)
//        return new OAuth2AccessTokenInterceptor(CLIENT_REGISTRATION_ID, oAuth2AuthorizedClientManager);
//    }
//}
