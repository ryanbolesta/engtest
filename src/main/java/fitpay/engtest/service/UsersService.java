package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitpay.engtest.configuration.FitPayAPIProperties;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.model.CreditCard;
import fitpay.engtest.model.Device;
import fitpay.engtest.model.UserAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UsersService {

    @Autowired
    private FitPayAPIProperties fitPayAPIProperties;

    @Autowired
    private RestTemplate fitpayRestTemplate;

    //TODO: Handle API errors, call devices/cards in parallel

    /**
     * Creates a CompositeUser by utilizing the FitPay API.
     * @param userId - Unique identifier for a user
     * @return CompositeUser object with a userId, list of devices, and list of credit cards
     * @throws JsonProcessingException
     */
    public CompositeUser getCompositeUser(String userId) throws JsonProcessingException {

        ResponseEntity<String> usersResponse = getUser(userId);
        CompositeUser compositeUser = new CompositeUser();

        if (usersResponse.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode links = mapper.readTree(Objects.requireNonNull(usersResponse.getBody())).path("_links");

            String devicesLink = links.path(fitPayAPIProperties.getDevicesLink()).path("href").asText();
            compositeUser.setDevices(getUserAssetList(Device[].class, devicesLink));

            String cardsLink = links.path(fitPayAPIProperties.getCardsLink()).path("href").asText();
            compositeUser.setCreditCards(getUserAssetList(CreditCard[].class, cardsLink));

            compositeUser.setUserId(userId);
            return compositeUser;
        }
        return null;
    }

    /**
     * Makes API call to retrieve a list of assets of a user (devices, credit cards)
     * Precondition is that the API call has an attribute in the response named "results" which holds a list of assets
     * @param c - Class array that extends UserAsset
     * @param url - API url to retrieve list of assets
     * @return List of user assets
     * @throws JsonProcessingException
     */
    private <T extends UserAsset> List<T> getUserAssetList(Class<T[]> c, String url) throws JsonProcessingException {
        ResponseEntity<String> response = getAPIResponse(url);
        ObjectMapper mapper = new ObjectMapper();
        if (response.getStatusCode().is2xxSuccessful()) {
            String results = mapper.readTree(Objects.requireNonNull(response.getBody())).get("results").toString();
            return Arrays.asList(mapper.readValue(results, c));
        }
        return null;
    }

    /**
     * Calls fitpay API for the individual user call.
     * @param userId - unique id for the user
     * @return - FitPay API response in the form of a ResponseEntity
     */
    private ResponseEntity<String> getUser(String userId) {
        return getAPIResponse(fitPayAPIProperties.getBaseUrl().concat("/").concat(userId));
    }

    /**
     * Utilizes restTemplate bean to call external API
     * @param url - url of the API endpoint
     * @return - API response in the form of a ResponseEntity
     */
    private ResponseEntity<String> getAPIResponse(String url) {
        HttpEntity<String> entity = new HttpEntity<>("body", getHttpHeaders());
        return fitpayRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    /**
     * Creates and returns HttpHeaders object with the Accept and BearerAuth headers set
     * @return HttpHeaders object to be used with RestTemplate
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(fitPayAPIProperties.getAccessToken());
        return headers;
    }

}
