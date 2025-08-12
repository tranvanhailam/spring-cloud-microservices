package vnpt_it.vn.accountservice.util.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import vnpt_it.vn.accountservice.exception.NotFoundException;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404 && methodKey.startsWith("CompanyService#getCompanyById")) {
            return new NotFoundException("Company not found");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}

