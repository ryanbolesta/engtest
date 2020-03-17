package fitpay.engtest.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception to be used when FitPay API returns a response with an unexpected status
 */
public class FitPayAPIException extends Exception {
    public FitPayAPIException(String errorMessage, HttpStatus status) {
        super(errorMessage + "\nFitPay API Response:\t" + status.value() + " " + status.getReasonPhrase());
    }
}
