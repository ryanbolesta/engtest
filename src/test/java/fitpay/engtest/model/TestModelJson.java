package fitpay.engtest.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fitpay.engtest.utility.ResourceReader.readFileToString;
import static org.assertj.core.api.Assertions.*;

/**
* Tests for serializing and deseserializing JSON in the application
**/
@RunWith(SpringRunner.class)
@AutoConfigureJsonTesters
public class TestModelJson {

    @Autowired
    private JacksonTester<CompositeUser> compositeUserJacksonTester;

    @Autowired
    private JacksonTester<Token> tokenJacksonTester;

    @Autowired
    private JacksonTester<User> userJacksonTester;

    @Test
    public void serializeCompositeUserTest() throws IOException {

        Device device_1 = new Device("123");
        device_1.setState("INITIALIZED");
        List<Device> devices = List.of(device_1);

        CreditCard creditCard_1 = new CreditCard("789");
        creditCard_1.setState("INITIALIZED");
        List<CreditCard> creditCards = List.of(creditCard_1);

        CompositeUser user = new CompositeUser();
        user.setUserId("123xyz");
        user.setDevices(devices);
        user.setCreditCards(creditCards);

        assertThat(compositeUserJacksonTester.write(user))
                .isEqualToJson(new ClassPathResource("json/compositeUserResponse.json"));
    }

    @Test
    public void deserializeTokenTest() throws IOException {
        Token token = new Token("token");
        String tokenResponse = readFileToString("json/tokenResponse.json");
        assertThat(tokenJacksonTester.parseObject(tokenResponse)).isEqualTo(token);
    }

    @Test
    public void deserializeUserTest() throws IOException {
        User expectedUser = new User();
        expectedUser.setUserId("123xyz");
        Map<String, Link> expectedLinks = new HashMap<>();
        expectedLinks.put("devices", new Link("https://api.qa.fitpay.ninja/users/123xyz/devices"));
        expectedLinks.put("creditCards", new Link ("https://api.qa.fitpay.ninja/users/123xyz/creditCards"));
        expectedUser.setLinks(expectedLinks);

        String userResponse = readFileToString("json/userResponse.json");
        User user = userJacksonTester.parseObject(userResponse);
        Map<String, Link> links = user.getLinks();

        Assert.assertEquals(expectedUser, user);
        Assert.assertEquals(expectedLinks.get("devices"), links.get("devices"));
        Assert.assertEquals(expectedLinks.get("users"), links.get("users"));

    }

}
