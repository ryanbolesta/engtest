package fitpay.engtest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    @GetMapping(path = "compositeUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCompositeUsers() {
        return ResponseEntity.ok("Placeholder for getCompositeUsers");
    }

}
