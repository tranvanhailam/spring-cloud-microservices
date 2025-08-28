package vnpt_it.vn.subscriberservice.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vnpt_it.vn.subscriberservice.auth.AuthorizationCodeFeignConfig;
import vnpt_it.vn.subscriberservice.auth.ResUserInfo;

@FeignClient(name = "auth-service", configuration = AuthorizationCodeFeignConfig.class)
public interface AuthService {
    @GetMapping("/userinfo")
    ResUserInfo getUserInfo();
}