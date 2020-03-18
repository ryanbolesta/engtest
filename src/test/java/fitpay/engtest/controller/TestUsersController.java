package fitpay.engtest.controller;

import fitpay.engtest.model.CompositeUser;
import fitpay.engtest.service.CompositeUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebClient
@WebMvcTest(CompositeUserController.class)
class TestUsersController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompositeUserService usersService;

    @Test
    void shouldReturnUserWithNullAssets() throws Exception {
        CompositeUser user = new CompositeUser();
        user.setUserId("123");
        when(usersService.getCompositeUser("123", null, null)).thenReturn(user);
        this.mockMvc.perform(get("/compositeUsers/123"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\"userId\":\"123\",\"devices\":null,\"creditCards\": null}"));
    }

}
