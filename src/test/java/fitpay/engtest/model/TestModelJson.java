package fitpay.engtest.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@AutoConfigureJsonTesters
public class TestModelJson {

    @Autowired
    private JacksonTester<CompositeUser> json;

    @Test
    public void serializeCompositeUserTest() throws IOException {

        Device device_1 = new Device("123");
        device_1.setState("INITIALIZED");
        List<Device> devices = List.of(device_1);

        CreditCard creditCard_1 = new CreditCard("789");
        creditCard_1.setState("INITIALIZED");
        List<CreditCard> creditCards = List.of(creditCard_1);

        CompositeUser expectedUser = new CompositeUser();
        expectedUser.setUserId("123xyz");
        expectedUser.setDevices(devices); //Should be empty list because of filter
        expectedUser.setCreditCards(creditCards); //Filter should exclude credit_card2

        assertThat(json.write(expectedUser)).isEqualToJson(new ClassPathResource("json/compositeUserResponse.json"));
    }
}
