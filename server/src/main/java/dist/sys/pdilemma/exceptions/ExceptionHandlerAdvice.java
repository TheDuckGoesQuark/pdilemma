package dist.sys.pdilemma.exceptions;

import dist.sys.pdilemma.models.ErrorMessageModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(value = BaseException.class)
    protected ResponseEntity<ErrorMessageModel> handleNotFound(RuntimeException ex, WebRequest request) {
        BaseException baseException = (BaseException) ex;
        return new ResponseEntity<>(new ErrorMessageModel(baseException.getMessage()), baseException.getStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ErrorMessageModel> handleConstraintViolation(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessageModel(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
