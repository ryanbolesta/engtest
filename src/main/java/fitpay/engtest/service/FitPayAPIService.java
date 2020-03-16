package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitpay.engtest.model.User;
import fitpay.engtest.model.UserAsset;
import fitpay.engtest.properties.FitPayAPIProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FitPayAPIService {
    private final String BODY = "body";
    private final String SLASH = "/";
    private final String RESULTS = "results";

    @Autowired
    private FitPayAPIProperties fitPayAPIProperties;

    @Autowired
    private RestTemplate fitpayRestTemplate;

    /**
     * Calls fitpay API for the individual user call.
     * @param userId - unique id for the user
     * @return - FitPay API response in the form of a ResponseEntity
     */
    User getUser(String userId) {
        String url = fitPayAPIProperties.getBaseUrl().concat(SLASH).concat(userId);
        return getAPIResponse(url, User.class);
    }

    /**
     * Makes API call to retrieve a list of assets of a user (devices, credit cards)
     * Precondition is that the API call has an attribute in the response named "results" which holds a list of assets
     * @param c - Class array that extends UserAsset
     * @return List of user assets
     * @throws JsonProcessingException
     */
    <T extends UserAsset> List<T> getUserAssetList(Class<T[]> c, String url) throws JsonProcessingException {
        String responseBody = getAPIResponse(url);
        ObjectMapper mapper = new ObjectMapper();
        String results = mapper.readTree(responseBody).get(RESULTS).toString();
        return Arrays.asList(mapper.readValue(results, c));
    }

    /**
     * Utilizes restTemplate bean to call external API
     * @param url - url of the API endpoint
     * @return - API response in the form of a ResponseEntity
     */
    private <T> T getAPIResponse(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(fitPayAPIProperties.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(BODY, headers);
        ResponseEntity<T> response = fitpayRestTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    /**
     * Overloaded for default case of a string response
     * @param url - url to connect to API
     * @return
     */
    private String getAPIResponse(String url) {
        return getAPIResponse(url, String.class);
    }

}
