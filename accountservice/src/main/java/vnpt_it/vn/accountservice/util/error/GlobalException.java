package vnpt_it.vn.accountservice.util.error;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vnpt_it.vn.accountservice.domain.res.RestResponse;
import vnpt_it.vn.accountservice.exception.ExistsException;
import vnpt_it.vn.accountservice.exception.NotFoundException;
import vnpt_it.vn.accountservice.exception.ValidationException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }


    @ExceptionHandler(value = FeignException.NotFound.class)
    public ResponseEntity<RestResponse<Object>> handleFeignExceptionNotFound(FeignException.NotFound e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        restResponse.setMessage(e.getMessage());
        restResponse.setError("Feign Exception Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NotFoundException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        restResponse.setMessage(e.getMessage());
        restResponse.setError("Not Found Exception");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
    }

    @ExceptionHandler(value = ExistsException.class)
    public ResponseEntity<RestResponse<Object>> handleExistsException(ExistsException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(e.getMessage());
        restResponse.setError("Exists Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationException(ValidationException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setMessage(e.getMessage());
        restResponse.setError("Validation Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
