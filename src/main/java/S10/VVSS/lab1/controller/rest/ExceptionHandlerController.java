package S10.VVSS.lab1.controller.rest;

import S10.VVSS.lab1.dto.FailureDto;
import S10.VVSS.lab1.exception.AuthException;
import S10.VVSS.lab1.exception.BadRequestException;
import S10.VVSS.lab1.exception.NotFoundException;
import S10.VVSS.lab1.exception.UnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public FailureDto authExceptionHandler(AuthException ex){
        return new FailureDto(ex);
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FailureDto authExceptionHandler(NotFoundException ex){
        return new FailureDto(ex);
    }

    @ExceptionHandler(value = UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public FailureDto authExceptionHandler(UnprocessableEntityException ex){
        return new FailureDto(ex);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailureDto authExceptionHandler(BadRequestException ex){
        return new FailureDto(ex);
    }
}
