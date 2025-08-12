package vnpt_it.vn.accountservice.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service", configuration = AuthorizationCodeFeignConfig.class)
public interface AuthService {
    @GetMapping("/userinfo")
    ResUserInfo getUserInfo();
}