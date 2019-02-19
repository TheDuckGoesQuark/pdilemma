package dist.sys.pdilemma.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class Handler extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        BaseException baseException = (BaseException) ex;
        return handleExceptionInternal(baseException, ex.getMessage(), new HttpHeaders(), baseException.getStatus(), request);
    }

}