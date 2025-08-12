package vnpt_it.vn.accountservice.company;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

//@Configuration
public class ClientCredentialFeignConfig implements RequestInterceptor {
    //config gọi đến authorization server để lấy về token,
    //tự động chèn vào RequestInterceptor của open feign gửi lên resource server (company service)
    private final OAuth2AuthorizedClientManager clientManager;

    public ClientCredentialFeignConfig(OAuth2AuthorizedClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId("company-service")
                .principal("internal")
                .build();
        OAuth2AuthorizedClient client = clientManager.authorize(request);
        if (client != null && client.getAccessToken() != null) {
            requestTemplate.header("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
            System.out.println("----------ClientCredential: " + client.getAccessToken().getTokenValue());
        }
    }
}
