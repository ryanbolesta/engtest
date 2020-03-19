package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitpay.engtest.exception.FitPayAPIException;
import fitpay.engtest.model.Token;
import fitpay.engtest.model.User;
import fitpay.engtest.model.UserAsset;
import fitpay.engtest.properties.FitPayAPIProperties;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Service provides functions for interacting with the FitPay API
 */
@Service
public class FitPayAPIService {
    private final Logger LOGGER = LoggerFactory.getLogger(FitPayAPIService.class);

    private final String BODY = "body";
    private final String SLASH = "/";
    private final String COLON = ":";
    private final String RESULTS = "results";

    @Autowired
    private FitPayAPIProperties fitPayAPIProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Token fitPayAPIAccessToken;

    /**
     * Retrieves a User from the FitPay API individual user endpoint for the given userId
     * @param userId - Unique identifier for a user used as a path parameter to the FitPay API
     * @return - Newly created user object mapped from the API response
     * @throws FitPayAPIException - Will occur if the response status is not in the expected 200 range
     */
    User getUser(String userId) throws FitPayAPIException {
        String url = fitPayAPIProperties.getBaseUrl().concat(SLASH).concat(userId);
        return makeGetRequest(url, User.class);
    }

    /**
     * Makes API call to retrieve a list of assets of a user (devices, credit cards)
     * Assumed that the API call has an attribute in the response named "results" which holds a list of assets
     * @param c - type of the List of data to be returned, must be a subclass of UserAsset
     * @param url - Absolute URL of the API endpoint
     * @return List of a subclass of UserAsset
     * @throws JsonProcessingException - Will occur when retrieving an unexpected JSON response likely because the
     *                                  "results" attribute is not in the response.
     * @throws FitPayAPIException - Will occur if the response status is not in the expected 200 range
     */
    <T extends UserAsset> List<T> getUserAssetList(Class<T[]> c, String url) throws JsonProcessingException, FitPayAPIException {
        String responseBody = makeGetRequest(url);

        LOGGER.debug("Parsing FitPay API response for attribute={} from URL={} with response body={}", RESULTS, url,
                responseBody);
        ObjectMapper mapper = new ObjectMapper();
        String results = mapper.readTree(responseBody).get(RESULTS).toString();
        return Arrays.asList(mapper.readValue(results, c));
    }

    /**
     * Makes a get request to the FitPay API to accept a JSON response specified by the Accept header.
     * The access token bean is included in the Bearer Auth header.
     * @param url - Absolute URL of the API endpoint
     * @param responseType Class return type of the response body
     * @return Body of the api response with type of the responseType parameter
     * @throws FitPayAPIException - Will occur if the response status is not in the expected 200 range
     */
    private <T> T makeGetRequest(String url, Class<T> responseType) throws FitPayAPIException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(fitPayAPIAccessToken.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(BODY, headers);

        LOGGER.debug("Making GET request to FitPay API for URL={}", url);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new FitPayAPIException("Unable to retrieve resource from FitPay API", response.getStatusCode());
    }

    /**
     * Overloaded for default case of a string response
     * @param url - url to connect to API
     * @return The string response body
     */
    private String makeGetRequest(String url) throws FitPayAPIException {
        return makeGetRequest(url, String.class);
    }

    /**
     * Retrieves access token from the FitPay API via the Base64 encoded clientId and secret properties added to
     * the BasicAuth header
     * @return The retrieved string access token
     * @throws FitPayAPIException - Will occur if the response status is not in the expected 200 range
     * @throws JsonProcessingException - Will occur if the JSON response structure is unexpected and cannot be parsed
     */
    public Token getAccessToken() throws FitPayAPIException, JsonProcessingException {
        String clientId = fitPayAPIProperties.getClientId();
        String secret = fitPayAPIProperties.getSecret();
        String credentials = clientId + COLON + secret;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(encodedCredentials);
        String tokenUrl = fitPayAPIProperties.getTokenUrl();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        LOGGER.debug("Calling FitPay API to retrieve access token. tokenUrl={}", tokenUrl);
        ResponseEntity<Token> response = restTemplate.exchange(tokenUrl, HttpMethod.GET, entity, Token.class);

        if (response.getStatusCode().is2xxSuccessful() && null != response.getBody()) {
            LOGGER.debug("Success retrieving access token value={}", response.getBody());
            return response.getBody();
        }

        throw new FitPayAPIException("Unable to retrieve access token from FitPay API", response.getStatusCode());
    }

}
