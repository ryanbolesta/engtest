package fitpay.engtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.exception.FitPayAPIException;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.service.CompositeUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * Rest mappings for CompositeUser endpoints, exceptions are handled in AppControllerAdvice
 */
@RestController
@RequestMapping("compositeUsers")
public class CompositeUserController {
    private final Logger LOGGER = LoggerFactory.getLogger(CompositeUserController.class);

    @Autowired
    private CompositeUserService compositeUserService;

    /**
     * GET request mapping to retrieve a CompositeUser by utilizing the FitPay API
     * @param userId unique identifier of the user
     * @param deviceState optional state filter for the user's devices
     * @param creditCardState optional state filter for the user's credit cards
     */
    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompositeUser> getCompositeUsers(@PathVariable String userId,
                                                           @RequestParam(required = false) String deviceState,
                                                           @RequestParam(required = false) String creditCardState)
            throws InterruptedException, ExecutionException, FitPayAPIException, JsonProcessingException {

        LOGGER.debug("Request received to create composite user for userId={}", userId);
        CompositeUser compositeUser = compositeUserService.getCompositeUser(userId, deviceState, creditCardState);
        return ResponseEntity.ok(compositeUser);
    }

}
