package dist.sys.pdilemma.exceptions;

import org.springframework.http.HttpStatus;

public class PreconditionFailedException extends BaseException {
    public PreconditionFailedException(String message) {
        super(HttpStatus.PRECONDITION_FAILED, message);
    }
}
