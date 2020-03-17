package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final String DEVICES = "devices";
    private final String CREDIT_CARDS = "creditCards";

    @Autowired
    private FitPayAPIService fitPayAPIService;

    /**
     * Creates a CompositeUser by utilizing the FitPay API.
     * @param userId - Unique identifier for a user
     * @return CompositeUser object with a userId, list of devices, and list of credit cards
     * @throws JsonProcessingException
     */
    public CompositeUser getCompositeUser(String userId, String deviceFilter, String creditCardFilter)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        CompositeUser compositeUser = new CompositeUser();
        User user = fitPayAPIService.getUser(userId);
        Map<String, Link> links = user.getLinks();

        CompletableFuture<List<Device>> deviceListFuture = getDeviceListFuture(links, deviceFilter);
        CompletableFuture<List<CreditCard>> cardListFuture = getCreditCardListFuture(links, creditCardFilter);

        compositeUser.setUserId(user.getId());
        compositeUser.setDevices(deviceListFuture.get());
        compositeUser.setCreditCards(cardListFuture.get());

        return compositeUser;
    }

    @Async
    CompletableFuture<List<Device>> getDeviceListFuture(Map<String, Link> linkMap, String deviceFilter) throws JsonProcessingException {
        String url = linkMap.get(DEVICES).getHref();
        List<Device> deviceList = fitPayAPIService.getUserAssetList(Device[].class, url);
        deviceList = filterResults(deviceList, deviceFilter);
        return CompletableFuture.completedFuture(deviceList);
    }

    @Async
    CompletableFuture<List<CreditCard>> getCreditCardListFuture(Map<String, Link> linkMap, String creditCardFilter) throws JsonProcessingException {
        String url = linkMap.get(CREDIT_CARDS).getHref();
        List<CreditCard> creditCardList = fitPayAPIService.getUserAssetList(CreditCard[].class, url);
        creditCardList = filterResults(creditCardList, creditCardFilter);
        return CompletableFuture.completedFuture(creditCardList);
    }

    private <T extends UserAsset> List<T> filterResults(List<T> assetList, String stateFilter) {
        if (null != stateFilter) {
            Predicate<UserAsset> stateEquals = userAsset -> stateFilter.equals(userAsset.getState());
            return assetList.stream().filter(stateEquals).collect(Collectors.toList());
        }
        return assetList;
    }

}
