package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.exception.FitPayAPIException;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.model.CreditCard;
import fitpay.engtest.model.Device;
import fitpay.engtest.model.Link;
import fitpay.engtest.model.User;
import fitpay.engtest.model.UserAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service with functions for handling Users, including creating a CompositeUser
 */
@Service
public class CompositeUserService {
    private final Logger LOGGER = LoggerFactory.getLogger(CompositeUserService.class);

    private final String DEVICES = "devices";
    private final String CREDIT_CARDS = "creditCards";

    @Autowired
    private FitPayAPIService fitPayAPIService;

    /**
     * Creates a CompositeUser by utilizing the FitPay API. First, an individual User is retrieved from the API, which
     * includes a map of API links to other assets of the user. We use these links to then retrieve a list of
     * Devices and CreditCards associated with the user. The Devices and CreditCards are retrieved in parallel by
     * utilizing CompletableFuture.
     * @param userId - Unique identifier for a user
     * @return CompositeUser object with a userId, list of devices, and list of credit cards
     */
    public CompositeUser getCompositeUser(String userId, String deviceFilter, String creditCardFilter)
            throws JsonProcessingException, ExecutionException, InterruptedException, FitPayAPIException {
        CompositeUser compositeUser = new CompositeUser();

        LOGGER.debug("Retrieving individual user from FitPay API for user with id={}", userId);
        User user = fitPayAPIService.getUser(userId);
        Map<String, Link> links = user.getLinks();

        LOGGER.debug("Retrieving devices and credit cards for user with id={}", userId);
        CompletableFuture<List<Device>> deviceListFuture = getDeviceListFuture(links, deviceFilter);
        CompletableFuture<List<CreditCard>> cardListFuture = getCreditCardListFuture(links, creditCardFilter);
        compositeUser.setUserId(user.getId());
        compositeUser.setDevices(deviceListFuture.get());
        compositeUser.setCreditCards(cardListFuture.get());

        LOGGER.debug("Successfully created Composite User object for user with id={}", userId);
        return compositeUser;
    }

    /**
     * Calls FitPay API to retrieve a List of Device for a user. The URL to the API endpoint
     * is retrieved via the passed in Map
     * @param linkMap - Map of links retrieved from the FitPay API individual user call
     * @param deviceFilter - String filter, only include items in the list with a state that matches this value
     * @return CompletableFuture with a List of Device for the user
     */
    @Async
    CompletableFuture<List<Device>> getDeviceListFuture(Map<String, Link> linkMap, String deviceFilter)
            throws JsonProcessingException, FitPayAPIException {
        String url = linkMap.get(DEVICES).getHref();
        List<Device> deviceList = fitPayAPIService.getUserAssetList(Device[].class, url);

        LOGGER.debug("Successfully retrieved device list from fitpay API, now will filter list with filter={}",
                deviceFilter);
        deviceList = filterResults(deviceList, deviceFilter);
        return CompletableFuture.completedFuture(deviceList);
    }

    /**
     * Calls FitPay API to retrieve list of CreditCards for a user. The URL to the API endpoint
     * is retrieved via the passed in Map
     * @param linkMap - Map of links retrieved from the FitPay API individual user call
     * @param creditCardFilter - String filter, only include items in the list with a state that matches this value
     * @return CompletableFuture with a List of CreditCard for the user
     */
    @Async
    CompletableFuture<List<CreditCard>> getCreditCardListFuture(Map<String, Link> linkMap, String creditCardFilter)
            throws JsonProcessingException, FitPayAPIException {
        String url = linkMap.get(CREDIT_CARDS).getHref();
        List<CreditCard> creditCardList = fitPayAPIService.getUserAssetList(CreditCard[].class, url);

        LOGGER.debug("Successfully retrieved credit card list from fitpay API, now will filter list with filter={}",
                creditCardFilter);
        creditCardList = filterResults(creditCardList, creditCardFilter);
        return CompletableFuture.completedFuture(creditCardList);
    }

    /**
     * Filters the passed in asset list to only include assets with a state equal to stateFilter
     * @param assetList - List of a class that extends UserAsset
     * @param stateFilter - Filter value
     * @return Filtered list of class that extends UserAsset
     */
    private <T extends UserAsset> List<T> filterResults(List<T> assetList, String stateFilter) {
        if (null != stateFilter) {
            Predicate<UserAsset> stateEquals = userAsset -> stateFilter.equals(userAsset.getState());
            return assetList.stream().filter(stateEquals).collect(Collectors.toList());
        }
        return assetList;
    }

}
