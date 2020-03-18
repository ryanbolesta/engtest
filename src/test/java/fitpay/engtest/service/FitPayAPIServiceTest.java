package fitpay.engtest.service;

import fitpay.engtest.SpringTestConfig;
import fitpay.engtest.exception.FitPayAPIException;
import fitpay.engtest.model.CreditCard;
import fitpay.engtest.model.Device;
import fitpay.engtest.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static fitpay.engtest.utility.ResourceReader.readFileToString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class FitPayAPIServiceTest {

    @MockBean
    private RestTemplate fitPayRestTemplate;

    @Autowired
    private FitPayAPIService fitPayAPIService;

    /**
     *  Tests getUser in FitPayAPIService by mocking RestTemplate to return the expected user
     */
    @Test
    public void givenMockingIsDoneByMockito_whenGetUserIsCalled_shouldReturnMockedObject() throws FitPayAPIException {
        User expectedUser = new User();
        expectedUser.setId("123xyz");
        Mockito
                .when(fitPayRestTemplate.exchange(eq("https://api.qa.fitpay.ninja/users/123xyz"), eq(HttpMethod.GET), any(), eq(User.class)))
          .thenReturn(new ResponseEntity<>(expectedUser, HttpStatus.OK));

        User user = fitPayAPIService.getUser("123xyz");
        Assert.assertEquals(expectedUser, user);
    }

    /**
     * Tests getUserAssetList for CreditCard in FitPayAPIService. Mocks RestTemplate to return a ResponseEntity
     * holding an expected JSON response string.
     */
    @Test
    public void givenMockingIsDoneByMockito_whenGetCreditCardsIsCalled_shouldReturnMockedObject() throws FitPayAPIException, IOException {
        String url = "https://api.qa.fitpay.ninja/users/123xyz/creditCards";
        String jsonResponse = readFileToString("json/creditCardResponse.json");
        CreditCard expectedCard_1 = new CreditCard("789abc");
        CreditCard expectedCard_2 = new CreditCard("555aaa");
        List<CreditCard> expectedCreditCards = List.of(expectedCard_1, expectedCard_2);
        Mockito
                .when(fitPayRestTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));

        List<CreditCard> creditCardList = fitPayAPIService.getUserAssetList(CreditCard[].class, url);
        Assert.assertArrayEquals(expectedCreditCards.toArray(), creditCardList.toArray());
    }

    /**
     * Tests getUserAssetList for Device in FitPayAPIService. Mocks RestTemplate to return a ResponseEntity
     * holding an expected JSON response string.
     */
    @Test
    public void givenMockingIsDoneByMockito_whenGetDevicesIsCalled_shouldReturnMockedObject() throws FitPayAPIException, IOException {
        String url = "https://api.qa.fitpay.ninja/users/123xyz/devices";
        String jsonResponse = readFileToString("json/devicesResponse.json");
        Device expectedDevice_1 = new Device("000");
        Device expectedDevice_2 = new Device("111");
        List<Device> expectedDevices = List.of(expectedDevice_1, expectedDevice_2);
        Mockito
                .when(fitPayRestTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));

        List<Device> deviceList = fitPayAPIService.getUserAssetList(Device[].class, url);
        Assert.assertArrayEquals(expectedDevices.toArray(), deviceList.toArray());
    }

    //TODO: Access token test

}
