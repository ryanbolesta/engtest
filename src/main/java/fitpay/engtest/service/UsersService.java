package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitpay.engtest.configuration.FitPayAPIProperties;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.model.CreditCard;
import fitpay.engtest.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Service
public class UsersService {

    @Autowired
    private FitPayAPIProperties fitPayAPIProperties;

    @Autowired
    private RestTemplate fitpayRestTemplate;

    //TODO: Handle API errors, call devices/cards in parallel
    public CompositeUser getCompositeUser(String userId) throws JsonProcessingException {

        ResponseEntity<String> usersResponse = getUsers(userId);
        CompositeUser compositeUser = new CompositeUser();

        if (usersResponse.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode links = mapper.readTree(Objects.requireNonNull(usersResponse.getBody())).path("_links");
            ResponseEntity<String> devicesResponse = getAPIResponse(links.path(fitPayAPIProperties.getDevicesLink()).path("href").asText());
            ResponseEntity<String> cardsResponse = getAPIResponse(links.path(fitPayAPIProperties.getCardsLink()).path("href").asText());

            if (devicesResponse.getStatusCode().is2xxSuccessful()) {
                String results = mapper.readTree(Objects.requireNonNull(devicesResponse.getBody())).get("results").toString();
                compositeUser.setDevices(Arrays.asList(mapper.readValue(results, Device[].class)));
            }

            if (cardsResponse.getStatusCode().is2xxSuccessful()) {
                String results = mapper.readTree(Objects.requireNonNull(cardsResponse.getBody())).path("results").toString();
                compositeUser.setCreditCards(Arrays.asList(mapper.readValue(results, CreditCard[].class)));
            }

            compositeUser.setUserId(userId);
            return compositeUser;
        }
        return null;
    }

    private ResponseEntity<String> getUsers(String userId) {
        return getAPIResponse(fitPayAPIProperties.getBaseUrl().concat("/").concat(userId));
    }

    private ResponseEntity<String> getAPIResponse(String path) {
        HttpEntity<String> entity = new HttpEntity<>("body", getHttpHeaders());
        return fitpayRestTemplate.exchange(path, HttpMethod.GET, entity, String.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(fitPayAPIProperties.getAccessToken());
        return headers;
    }

}
