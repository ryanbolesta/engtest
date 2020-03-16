package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitpay.engtest.model.*;
import fitpay.engtest.properties.FitPayAPIProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final String DEVICES = "devices";
    private final String CREDIT_CARDS = "creditCards";
    private final String SLASH = "/";
    private final String RESULTS = "results";
    private final String BODY = "body";

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
    public CompositeUser getCompositeUser(String userId, String deviceFilter, String creditCardFilter)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        CompositeUser compositeUser = new CompositeUser();
        User user = getUser(userId);
        Map<String, Link> links = user.getLinks();

        CompletableFuture<List<Device>> deviceListFuture = getDeviceListFuture(links, deviceFilter);
        CompletableFuture<List<CreditCard>> cardListFuture = getCreditCardListFuture(links, creditCardFilter);

        compositeUser.setDevices(deviceListFuture.get());
        compositeUser.setCreditCards(cardListFuture.get());
        compositeUser.setUserId(user.getId());

        return compositeUser;
    }

    @Async
    CompletableFuture<List<Device>> getDeviceListFuture(Map<String, Link> linkMap, String deviceFilter) throws JsonProcessingException {
        String url = linkMap.get(DEVICES).getHref();
        List<Device> deviceList = getUserAssetList(Device[].class, url, deviceFilter);
        return CompletableFuture.completedFuture(deviceList);
    }

    @Async
    CompletableFuture<List<CreditCard>> getCreditCardListFuture(Map<String, Link> linkMap, String creditCardFilter) throws JsonProcessingException {
        String url = linkMap.get(CREDIT_CARDS).getHref();
        List<CreditCard> creditCardList = getUserAssetList(CreditCard[].class, url, creditCardFilter);
        return CompletableFuture.completedFuture(creditCardList);
    }

    /**
     * Makes API call to retrieve a list of assets of a user (devices, credit cards)
     * Precondition is that the API call has an attribute in the response named "results" which holds a list of assets
     * @param c - Class array that extends UserAsset
     * @return List of user assets
     * @throws JsonProcessingException
     */
    private <T extends UserAsset> List<T> getUserAssetList(Class<T[]> c, String url, String stateFilter) throws JsonProcessingException {
        ResponseEntity<String> response = getAPIResponse(url);
        String responseBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<T> assetList = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful() && null != responseBody) {
            String results = mapper.readTree(responseBody).get(RESULTS).toString();
            assetList = Arrays.asList(mapper.readValue(results, c));
            assetList = assetList.stream()
                                .filter(userAsset -> stateFilter.equals(userAsset.getState()))
                                .collect(Collectors.toList());
        }

        return assetList;
    }

    /**
     * Calls fitpay API for the individual user call.
     * @param userId - unique id for the user
     * @return - FitPay API response in the form of a ResponseEntity
     */
    private User getUser(String userId) {
        String url = fitPayAPIProperties.getBaseUrl().concat(SLASH).concat(userId);
        HttpEntity<String> entity = new HttpEntity<>(BODY, getHttpHeaders());
        ResponseEntity<User> response = fitpayRestTemplate.exchange(url, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }

    /**
     * Utilizes restTemplate bean to call external API
     * @param url - url of the API endpoint
     * @return - API response in the form of a ResponseEntity
     */
    private ResponseEntity<String> getAPIResponse(String url) {
        HttpEntity<String> entity = new HttpEntity<>(BODY, getHttpHeaders());
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
