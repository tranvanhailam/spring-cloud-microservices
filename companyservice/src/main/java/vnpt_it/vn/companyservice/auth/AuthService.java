package vnpt_it.vn.companyservice.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service")
public interface AuthService {
    @GetMapping("/userinfo")
    ResUserInfo getUserInfo();
}