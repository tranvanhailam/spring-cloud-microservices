package vnpt_it.vn.companyservice.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vnpt_it.vn.companyservice.domain.res.RestResponse;
import vnpt_it.vn.companyservice.exception.NotFoundException;
import vnpt_it.vn.companyservice.exception.ValidationException;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NotFoundException e) {
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        restResponse.setMessage(e.getMessage());
        restResponse.setError("Not Found Exception");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restResponse);
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
