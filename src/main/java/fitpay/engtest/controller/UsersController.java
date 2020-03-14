package fitpay.engtest.controller;

import fitpay.engtest.service.FitPayAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    @Autowired
    private FitPayAPIService fitPayAPIService;

    @GetMapping(path = "compositeUsers/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCompositeUsers(@PathVariable String userId) {
        return ResponseEntity.ok(fitPayAPIService.getUser(userId));
    }

}
