package vnpt_it.vn.gatewayservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vnpt_it.vn.gatewayservice.auth.AuthService;
import vnpt_it.vn.gatewayservice.auth.ResUserInfo;

@RestController
public class TestAPI {
    private final AuthService authService;

    public TestAPI(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth/userinfo")
    public ResUserInfo user() {
        return authService.getUserInfo();
    }
}
