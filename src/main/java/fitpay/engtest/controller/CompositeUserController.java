package fitpay.engtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.exception.FitPayAPIException;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.service.CompositeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rest mappings for CompositeUser endpoints
 */
@RestController
public class CompositeUserController {

    @Autowired
    private CompositeUserService compositeUserService;

    @GetMapping(path = "compositeUsers/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompositeUser> getCompositeUsers(@PathVariable String userId,
                                                           @RequestParam(required = false) String deviceState,
                                                           @RequestParam(required = false) String creditCardState) {
        try {
            CompositeUser compositeUser = compositeUserService.getCompositeUser(userId, deviceState, creditCardState);
            return ResponseEntity.ok(compositeUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing JSON when retrieving Composite User", e);
        } catch (FitPayAPIException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error connecting to the FitPay API when retrieving Composite User");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error occurred when retrieving Composite User", e);
        }
    }

}
