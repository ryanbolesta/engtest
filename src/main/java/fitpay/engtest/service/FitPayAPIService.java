package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitpay.engtest.model.User;
import fitpay.engtest.model.UserAsset;
import fitpay.engtest.properties.FitPayAPIProperties;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FitPayAPIService {
    private final String BODY = "body";
    private final String SLASH = "/";
    private final String COLON = ":";
    private final String RESULTS = "results";
    private final String ACCESS_TOKEN = "access_token";

    @Autowired
    private FitPayAPIProperties fitPayAPIProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String fitPayAPIAccessToken;

    public String getAccessToken() throws Exception {
        String clientId = fitPayAPIProperties.getClientId();
        String secret = fitPayAPIProperties.getSecret();
        String credentials = clientId + COLON + secret;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(encodedCredentials);
        String tokenUrl = fitPayAPIProperties.getTokenUrl();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful() && null != response.getBody()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.getBody()).path(ACCESS_TOKEN).asText();
        }

        throw new Exception("Unable to retrieve access token from FitPay API");

    }


    /**
     * Calls fitpay API for the individual user call.
     * @param userId - unique id for the user
     * @return - FitPay API response in the form of a ResponseEntity
     */
    User getUser(String userId) {
        String url = fitPayAPIProperties.getBaseUrl().concat(SLASH).concat(userId);
        return makeGetRequest(url, User.class);
    }

    /**
     * Makes API call to retrieve a list of assets of a user (devices, credit cards)
     * Precondition is that the API call has an attribute in the response named "results" which holds a list of assets
     * @param c - Class array that extends UserAsset
     * @return List of user assets
     * @throws JsonProcessingException
     */
    <T extends UserAsset> List<T> getUserAssetList(Class<T[]> c, String url) throws JsonProcessingException {
        String responseBody = makeGetRequest(url);
        ObjectMapper mapper = new ObjectMapper();
        String results = mapper.readTree(responseBody).get(RESULTS).toString();
        return Arrays.asList(mapper.readValue(results, c));
    }

    /**
     * Utilizes restTemplate bean to call external API
     * @param url - url of the API endpoint
     * @return - API response in the form of a ResponseEntity
     */
    private <T> T makeGetRequest(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(fitPayAPIAccessToken);
        HttpEntity<String> entity = new HttpEntity<>(BODY, headers);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    /**
     * Overloaded for default case of a string response
     * @param url - url to connect to API
     * @return
     */
    private String makeGetRequest(String url) {
        return makeGetRequest(url, String.class);
    }

}
