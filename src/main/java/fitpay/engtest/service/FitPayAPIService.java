package fitpay.engtest.service;

import fitpay.engtest.configuration.FitPayAPIProperties;
import fitpay.engtest.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;

@Service
public class FitPayAPIService {

    @Autowired
    private FitPayAPIProperties properties;

    @Autowired
    private RestTemplate fitpayRestTemplate;

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(properties.getAccessToken());
        return headers;
    }

    public String getUser(String userId) {
        final String URL = MessageFormat.format(AppConstants.USERS_URL, userId);
        HttpEntity<String> entity = new HttpEntity<>("body", getHttpHeaders());
        return fitpayRestTemplate.exchange(URL, HttpMethod.GET, entity, String.class).getBody();
    }

}
