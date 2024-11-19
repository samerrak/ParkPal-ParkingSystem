package ca.mcgill.ecse321.PLMS.exception;

import org.springframework.http.HttpStatus;

public class PLMSException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private HttpStatus status;

    public PLMSException(HttpStatus status, String message) {
        super(message);

        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
