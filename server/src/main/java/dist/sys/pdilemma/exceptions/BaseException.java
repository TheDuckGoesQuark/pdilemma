package dist.sys.pdilemma.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends Throwable {

    private HttpStatus status;
    private String message;

    public BaseException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
