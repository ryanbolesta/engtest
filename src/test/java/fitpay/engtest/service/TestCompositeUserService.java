package fitpay.engtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fitpay.engtest.SpringTestConfig;
import fitpay.engtest.exception.FitPayAPIException;
import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.model.CreditCard;
import fitpay.engtest.model.Device;
import fitpay.engtest.model.Link;
import fitpay.engtest.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class TestCompositeUserService {

    @Autowired
    private CompositeUserService compositeUserService;

    @MockBean
    private FitPayAPIService fitPayAPIService;

    /**
     *  Tests getUser in FitPayAPIService by mocking RestTemplate to return the expected user
     */
    @Test
    public void whenGetCompositeUserIsCalled_shouldReturnMockedCompositeUser()
            throws FitPayAPIException, JsonProcessingException, ExecutionException, InterruptedException {
        User user = new User();
        Map<String, Link> map = new HashMap<>();
        map.put("devices", new Link("devicesLink.test"));
        map.put("creditCards", new Link("cardsLink.test"));
        user.setLinks(map);
        user.setUserId("123xyz");
        Mockito
                .when(fitPayAPIService.getUser("123xyz"))
                .thenReturn(user);

        Device device_1 = new Device("123");
        device_1.setState("ERROR");
        List<Device> devices = List.of(device_1);
        Mockito
                .when(fitPayAPIService.getUserAssetList(Device[].class, "devicesLink.test"))
                .thenReturn(devices);


        CreditCard creditCard_1 = new CreditCard("123");
        creditCard_1.setState("ACTIVE");
        CreditCard creditCard_2 = new CreditCard("789");
        creditCard_2.setState("ERROR");
        List<CreditCard> creditCards = List.of(creditCard_1, creditCard_2);
        Mockito
                .when(fitPayAPIService.getUserAssetList(CreditCard[].class, "cardsLink.test"))
                .thenReturn(creditCards);

        CompositeUser compositeUser = compositeUserService
                .getCompositeUser("123xyz", "ACTIVE", "ACTIVE");

        CompositeUser expectedUser = new CompositeUser();
        expectedUser.setUserId("123xyz");
        expectedUser.setDevices(new ArrayList<>()); //Should be empty list because of filter
        expectedUser.setCreditCards(List.of(creditCard_1)); //Filter should exclude credit_card2

        Assert.assertEquals(expectedUser, compositeUser);
        Assert.assertArrayEquals(expectedUser.getCreditCards().toArray(), compositeUser.getCreditCards().toArray());
        Assert.assertArrayEquals(expectedUser.getDevices().toArray(), compositeUser.getDevices().toArray());
    }

}
