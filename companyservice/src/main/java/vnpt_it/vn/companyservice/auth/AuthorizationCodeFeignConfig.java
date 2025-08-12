package vnpt_it.vn.companyservice.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class AuthorizationCodeFeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof Jwt jwt) {
            String tokenValue = jwt.getTokenValue();  // Lấy token thực
            System.out.println("Token gửi đến auth-service: " + tokenValue);  // Log token thực
            template.header("Authorization", "Bearer " + tokenValue);
        } else {
            System.out.println("Không có token hợp lệ trong SecurityContextHolder");
        }
    }
}
