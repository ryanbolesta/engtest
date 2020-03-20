package fitpay.engtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.exception.FitPayAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.ExecutionException;

/**
 * Class contains exception to status mappings used for all controllers
 */
@ControllerAdvice
public class AppControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Unable to connect to FitPay API")
    @ExceptionHandler(FitPayAPIException.class)
    public void fitPayAPIError() { }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Error processing JSON")
    @ExceptionHandler(JsonProcessingException.class)
    public void jsonError() { }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR,
            reason="Error occurred while performing asynchronous operations")
    @ExceptionHandler({ExecutionException.class, InterruptedException.class})
    public void asyncError() { }

}

