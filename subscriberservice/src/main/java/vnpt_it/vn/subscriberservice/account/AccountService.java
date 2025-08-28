package vnpt_it.vn.subscriberservice.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vnpt_it.vn.subscriberservice.domain.res.RestResponse;

@FeignClient(name = "account-service", configuration = ClientCredentialFeignConfig.class)
public interface AccountService {
    @GetMapping("/accounts/{id}")
    RestResponse<AccountDTO> getAccountById(@PathVariable("id") long id);
}
