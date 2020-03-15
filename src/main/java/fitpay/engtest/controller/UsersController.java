package fitpay.engtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping(path = "compositeUsers/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompositeUser> getCompositeUsers(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(usersService.getCompositeUser(userId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to process JSON response to return Composite User", e);
        }
    }

}
