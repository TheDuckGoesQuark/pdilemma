package dist.sys.pdilemma.exceptions;

import dist.sys.pdilemma.models.ErrorMessageModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    protected ResponseEntity<ErrorMessageModel> handleNotFound(RuntimeException ex, WebRequest request) {
        BaseException baseException = (BaseException) ex;
        return new ResponseEntity<>(new ErrorMessageModel(baseException.getMessage()), baseException.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();

        Iterator<ObjectError> iter = errors.iterator();

        while (iter.hasNext()) {
            FieldError fe = (FieldError) iter.next();
            sb.append(fe.getField());
            sb.append(" ");
            sb.append(fe.getDefaultMessage());

            if (iter.hasNext()) {
                sb.append(", and ");
            }
        }

        return new ResponseEntity<>(new ErrorMessageModel(sb.toString()), HttpStatus.BAD_REQUEST);
    }



}
