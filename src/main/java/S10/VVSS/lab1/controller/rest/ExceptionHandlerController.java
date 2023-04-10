package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.dto.FailureDto;
import S10.VVSS.lab1.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<FailureDto> authExceptionHandler(AuthException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailureDto(ex));
    }
}
